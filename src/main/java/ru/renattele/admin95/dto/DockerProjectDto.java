package ru.renattele.admin95.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DockerProjectDto {
    private Long id;
    private String name;
    private ContainerState state;
    private String link;
    private Set<TagDto> tags = new HashSet<>();

    public enum ContainerState {
        RUNNING,
        STOPPED,
        PAUSED,
        EXITED,
        UNKNOWN
    }
}
