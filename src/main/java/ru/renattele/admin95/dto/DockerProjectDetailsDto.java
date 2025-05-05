package ru.renattele.admin95.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DockerProjectDetailsDto {
    private Long id;
    private String name;
    private String content;
    private String logs;
}
