package ru.renattele.admin95.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.renattele.admin95.model.FileEntity;
import ru.renattele.admin95.repository.FileRepository;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FileRepositoryImpl implements FileRepository {
    private final File rootFile;
    public FileRepositoryImpl(@Value("${DOCKER_PROJECTS_PATH}") String path) {
       rootFile = new File(path);
       if (!rootFile.exists()) {
           if (!rootFile.mkdirs()) {
               throw new IllegalStateException("Cannot create directory " + rootFile.getAbsolutePath());
           }
       }
    }
    @Override
    public List<FileEntity> getFiles() {
        var files = rootFile.listFiles();
        if (Objects.isNull(files)) {
            log.error("Cannot list files under {}", rootFile);
            return List.of();
        }
        return Arrays.stream(files)
                .map(file -> {
                    try {
                        return fileToFileEntity(file);
                    } catch (IOException e) {
                        log.error("Cannot read file {}", file, e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(FileEntity::getName))
                .toList();
    }

    @Override
    public boolean saveFile(FileEntity fileEntity) {
        var file = fileEntityToFile(fileEntity);
        if (Objects.isNull(file)) return false;
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    log.error("File already exists: {}", file);
                }
            } catch (IOException e) {
                log.error("Cannot create file {}", file, e);
                return false;
            }
        }
        try (var writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(fileEntity.getContent());
            return true;
        } catch (IOException e) {
            log.error("Cannot write to file {}", file, e);
            return false;
        }
    }

    @Override
    public boolean deleteFile(String id) {
        var fileEntity = fileEntityFromId(id);
        var file = fileEntityToFile(fileEntity);
        if (Objects.isNull(file)) return false;
        if (!file.exists()) return false;

        return file.delete();
    }

    private File fileEntityToFile(FileEntity fileEntity) {
        var file = new File(rootFile, fileEntity.getName());
        if (!Objects.equals(file.getName(), fileEntity.getName())) {
            log.warn("Possible path traversal attack: {}", fileEntity.getName());
            return null;
        }
        return file;
    }

    private FileEntity fileEntityFromId(String id) {
        return new FileEntity(id, id, "");
    }

    private FileEntity fileToFileEntity(File file) throws IOException {
        try (var reader = new BufferedReader(new FileReader(file))) {
            var content = reader.lines().collect(Collectors.joining("\n"));
            return FileEntity.builder()
                    .id(file.getName())
                    .name(file.getName())
                    .content(content)
                    .build();
        }

    }
}
