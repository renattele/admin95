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
@Table(name = "backups")
public class BackupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "running", nullable = false)
    private Boolean running;

    @Column(name = "message")
    private String message;

    @Column(name = "size")
    private Long size;
}
