package ru.renattele.admin95.repository.docker;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.renattele.admin95.model.docker.ContainerEntity;
import ru.renattele.admin95.model.docker.ContainerState;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DockerGetContainersResponse {
    @JsonProperty("Created")
    private Long created;

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Image")
    private String image;

    @JsonProperty("State")
    private String state;

    @JsonProperty("Names")
    private List<String> names;

    @JsonProperty("Labels")
    private Map<String, String> labels;


    public ContainerEntity toEntity() {
        if (names.isEmpty()) {
            throw new IllegalStateException("Container names is empty, response: " + this);
        }

        // Needed to substring because names start with "/"
        var name = names.get(0).substring(1);
        return ContainerEntity.builder()
                .id(id)
                .name(name)
                .state(stringToContainerState(state))
                .labels(labels)
                .build();
    }

    private ContainerState stringToContainerState(String state) {
        switch (state) {
            case "running" -> {
                return ContainerState.RUNNING;
            }
            case "paused" -> {
                return ContainerState.PAUSED;
            }
            case "exited" -> {
                return ContainerState.EXITED;
            }
            default -> {
                return ContainerState.UNKNOWN;
            }
        }
    }
}
