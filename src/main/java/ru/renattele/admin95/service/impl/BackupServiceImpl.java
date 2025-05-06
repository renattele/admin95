package ru.renattele.admin95.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.renattele.admin95.dto.BackupDeleteQueryDto;
import ru.renattele.admin95.dto.BackupDto;
import ru.renattele.admin95.mapper.BackupDeleteQueryMapper;
import ru.renattele.admin95.mapper.BackupMapper;
import ru.renattele.admin95.model.BackupEntity;
import ru.renattele.admin95.repository.BackupRepository;
import ru.renattele.admin95.service.BackupService;
import ru.renattele.admin95.service.ProcessService;
import ru.renattele.admin95.util.ProcessUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class BackupServiceImpl implements BackupService {
    private final BackupMapper backupMapper;
    private final BackupRepository backupRepository;
    private final File backupDir;
    private final String backupCommandTemplate;
    private final String backupDateFormat;
    private final String backupNameTemplate;
    private final String backupTarget;
    private ProcessService processService;
    @PersistenceContext
    private EntityManager entityManager;
    private BackupDeleteQueryMapper backupDeleteQueryMapper;

    @Autowired
    public BackupServiceImpl(
            BackupMapper backupMapper,
            BackupRepository backupRepository,
            @Value("${backup.path}") String backupPath,
            @Value("${backup.command}") String backupCommandTemplate,
            @Value("${backup.date-format}") String backupDateFormat,
            @Value("${backup.name}") String backupNameTemplate,
            @Value("${backup.target}") String backupTarget,
            ProcessService processService, BackupDeleteQueryMapper backupDeleteQueryMapper) {
        this.backupMapper = backupMapper;
        this.backupRepository = backupRepository;
        backupDir = new File(backupPath);
        this.backupCommandTemplate = backupCommandTemplate;
        this.backupDateFormat = backupDateFormat;
        this.backupNameTemplate = backupNameTemplate;
        this.processService = processService;
        this.backupTarget = backupTarget;
        if (!backupDir.exists() && !backupDir.mkdirs()) {
            throw new RuntimeException("Failed to create backup directory: " + backupPath);
        }
        this.backupDeleteQueryMapper = backupDeleteQueryMapper;
    }

    @Override
    public List<BackupDto> getAll() {
        return backupRepository.findAll().stream()
                .map(backupMapper::toDto)
                .toList();
    }

    @Nullable
    @Override
    public BackupDto getById(Long id) {
        var entity = backupRepository.findById(id).orElse(null);
        if (entity == null) return null;
        return backupMapper.toDto(entity);
    }

    @Override
    public InputStream getBackupStream(BackupDto backupDto) {
        var backupEntity = backupMapper.toEntity(backupDto);
        var file = new File(backupDir, backupEntity.getName());
        if (!file.exists()) return null;
        try {
            return new FileInputStream(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to get backup stream: " + e.getMessage(), e);
        }
    }

    @Override
    @Async
    @Scheduled(cron = "${backup.cron}")
    public CompletableFuture<Void> runBackupInBackground() {
        var currentDateTime = LocalDateTime.now();
        var formatter = DateTimeFormatter.ofPattern(backupDateFormat);
        var backupName = backupNameTemplate.replace("{date}",
                currentDateTime.format(formatter));
        var backupEntity = BackupEntity.builder()
                .name(backupName)
                .running(true)
                .message("Backup is running")
                .timestamp(currentDateTime)
                .build();
        backupRepository.save(backupEntity);
        var backupFile = new File(backupDir, backupName);
        var message = runBackup(backupFile.getAbsolutePath());
        backupEntity.setRunning(false);
        backupEntity.setSize(backupFile.length());
        backupEntity.setMessage(message);
        backupRepository.save(backupEntity);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void deleteBackup(BackupDto backupDto) {
        var backupEntity = backupMapper.toEntity(backupDto);
        backupRepository.delete(backupEntity);
        var file = new File(backupDir, backupEntity.getName());
        if (file.exists() && !file.delete()) {
            throw new RuntimeException("Failed to delete backup file: " + file.getAbsolutePath());
        }
    }


    @Override
    public List<BackupDto> getBackupsMatching(BackupDeleteQueryDto queryDto) {
        var query = backupDeleteQueryMapper.toEntity(queryDto);
        var cb = entityManager.getCriteriaBuilder();
        var criteriaQuery = cb.createQuery(BackupEntity.class);
        var root = criteriaQuery.from(BackupEntity.class);

        var predicates = new ArrayList<>();

        if (query.ageAmount() != null && query.ageUnit() != null) {
            var cutoffDate = LocalDateTime.now().minus(query.ageAmount(), query.ageUnit());
            predicates.add(cb.lessThanOrEqualTo(root.get("timestamp"), cutoffDate));
        }

        if (query.minSize() != null || query.maxSize() != null) {
            List<Predicate> sizePredicates = new ArrayList<>();

            if (query.minSize() != null) {
                sizePredicates.add(cb.or(
                        cb.isNull(root.get("size")),
                        cb.greaterThanOrEqualTo(root.get("size"), query.minSize())
                ));
            }

            if (query.maxSize() != null) {
                sizePredicates.add(cb.or(
                        cb.isNull(root.get("size")),
                        cb.lessThanOrEqualTo(root.get("size"), query.maxSize())
                ));
            }

            Predicate sizePredicate;
            if (sizePredicates.size() == 1) {
                sizePredicate = sizePredicates.getFirst();
            } else {
                sizePredicate = cb.and(sizePredicates.toArray(new Predicate[0]));
            }

            if ((query.maxSize() != null &&
                    query.minSize() != null) &&
                    query.maxSize() < query.minSize()) {
                predicates.add(cb.not(sizePredicate));
            } else {
                predicates.add(sizePredicate);
            }
        }

        if (!predicates.isEmpty()) {
            criteriaQuery.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        var backupsToDelete = entityManager.createQuery(criteriaQuery).getResultList();
        return backupsToDelete.stream().map(backupMapper::toDto).toList();
    }

    @Override
    public void deleteBackupsMatching(BackupDeleteQueryDto query) {
        getBackupsMatching(query).forEach(this::deleteBackup);
    }

    private String runBackup(String backupFilePath) {
        var command = backupCommandTemplate
                .replace("{backup_file}", backupFilePath)
                .replace("{backup_target}", backupTarget);
        try {
            var process = processService.runShell(command);
            return ProcessUtil.readErrorStream(process);
        } catch (IOException e) {
            return e.getMessage();
        }
    }
}
