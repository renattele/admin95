package ru.renattele.admin95.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.renattele.admin95.model.SystemMetricsEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface SystemMetricsRepository extends JpaRepository<SystemMetricsEntity, Long> {
    @Query("SELECT sm FROM SystemMetricsEntity sm WHERE sm.timestamp BETWEEN :startTime AND :endTime ORDER BY sm.timestamp")
    List<SystemMetricsEntity> findMetricsInTimeRange(LocalDateTime startTime, LocalDateTime endTime);
}
