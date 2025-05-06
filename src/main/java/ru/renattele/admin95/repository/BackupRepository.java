package ru.renattele.admin95.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.renattele.admin95.model.BackupEntity;

public interface BackupRepository extends JpaRepository<BackupEntity, Long> {
}
