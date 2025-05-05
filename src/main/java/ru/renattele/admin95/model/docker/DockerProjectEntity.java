package ru.renattele.admin95.model.docker;

import jakarta.persistence.*;
import lombok.*;
import ru.renattele.admin95.model.TagEntity;

import java.util.HashSet;
import java.util.Set;

@Table(name = "docker_project")
@Entity
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DockerProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private ContainerState state;

    @OneToOne(cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    @JoinColumn(name = "details_id", nullable = false)
    @ToString.Exclude
    private DockerProjectDetailsEntity details = DockerProjectDetailsEntity.builder()
            .project(this)
            .logs("")
            .content("")
            .build();

    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.REFRESH
    )
    @JoinTable(
            name = "docker_project_tags",
            joinColumns = @JoinColumn(name = "docker_project_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<TagEntity> tags = new HashSet<>();

    @Column(name = "link")
    private String link;
}
