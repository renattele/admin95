package ru.renattele.admin95.security.details;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.renattele.admin95.model.UserEntity;

import java.util.Collection;

@RequiredArgsConstructor
public class UserEntityDetails implements UserDetails {
    private final UserEntity userEntity;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userEntity.getRoles()
                .stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .toList();
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getName();
    }

    @Override
    public boolean isEnabled() {
        return userEntity.getState() == UserEntity.State.OK;
    }

    @Override
    public boolean isAccountNonLocked() {
        return userEntity.getState() != UserEntity.State.DISABLED;
    }
}
