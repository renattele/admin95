package ru.renattele.admin95.model.docker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContainerEntity {
    private String id;
    private String name;
    private ContainerState state;
    private Map<String, String> labels;
}

