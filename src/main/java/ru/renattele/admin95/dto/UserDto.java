package ru.renattele.admin95.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private LocalDateTime createdAt;

    public enum State {
        OK, DISABLED
    }

    private Long id;
    private String name;
    private String password;
    private List<Role> roles;
    private State state;
    private LocalDateTime lastAccessedAt;

    @RequiredArgsConstructor
    @Getter
    public enum Role {
        ACCESS_DASHBOARD("access.dashboard"),
        ACCESS_CONTAINERS("access.containers"),
        ACCESS_TERMINAL("access.terminal"),
        ACCESS_BACKUPS("access.backups");

        private final String messageKey;
    }
}