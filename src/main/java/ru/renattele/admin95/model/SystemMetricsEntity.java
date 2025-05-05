package ru.renattele.admin95.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "system_metrics")
public class SystemMetricsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cpu_temp", nullable = false)
    private double cpuTemperature;

    @Column(name = "cpu_usage", nullable = false)
    private double cpuUsage;

    @Column(name = "ram_usage", nullable = false)
    private double ramUsage;

    @Column(name = "disk_usage", nullable = false)
    private double diskUsage;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}
