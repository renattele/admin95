package ru.renattele.admin95.security.details;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.renattele.admin95.repository.UserRepository;

@Service("userDetailsService")
@RequiredArgsConstructor
public class UserEntityDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername: " + username);
        return new UserEntityDetails(userRepository.findByNameEquals(username).orElseThrow(
                () -> new UsernameNotFoundException("User " + username + " not found")));
    }
}
