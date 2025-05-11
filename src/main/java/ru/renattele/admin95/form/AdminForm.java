package ru.renattele.admin95.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminForm {
    @NotBlank
    private String name;

    private String password;

    @JsonProperty("ACCESS_DASHBOARD")
    private boolean accessDashboard;
    @JsonProperty("ACCESS_CONTAINERS")
    private boolean accessContainers;
    @JsonProperty("ACCESS_TERMINAL")
    private boolean accessTerminal;
    @JsonProperty("ACCESS_BACKUPS")
    private boolean accessBackups;

    private boolean enabled;
}
