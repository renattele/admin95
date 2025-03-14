package ru.renattele.admin95.model.docker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DockerProjectEntity {
    private String name;
    private File configFile;
    private ContainerState state;
    private List<ContainerEntity> containers;
}
