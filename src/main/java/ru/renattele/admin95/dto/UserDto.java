package ru.renattele.admin95.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    public enum Role {
        ACCESS_DASHBOARD,
        ACCESS_CONTAINERS,
        ACCESS_TERMINAL,
        ACCESS_BACKUPS,
        ACCESS_USERS
    }
    public enum State {
        OK, DISABLED
    }
    private Long id;
    private String name;
    private String password;
    private List<Role> roles;
    private State state;
}