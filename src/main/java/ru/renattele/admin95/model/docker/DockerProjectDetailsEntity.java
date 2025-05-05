package ru.renattele.admin95.model.docker;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "docker_project_details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DockerProjectDetailsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false, length = 1000000)
    private String content = "";

    @Column(name = "logs", nullable = false, length = 1000000)
    private String logs = "";

    @OneToOne(mappedBy = "details",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            optional = false,
            orphanRemoval = true)
    @JoinColumn(name = "project_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private DockerProjectEntity project;
}