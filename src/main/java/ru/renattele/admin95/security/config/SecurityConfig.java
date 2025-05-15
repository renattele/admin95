package ru.renattele.admin95.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import ru.renattele.admin95.filter.FilterChainExceptionHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final FilterChainExceptionHandler exceptionHandlerFilter;
    @Bean
    public SecurityFilterChain webSecurity(HttpSecurity http) throws Exception {
        return http
                .csrf(c -> c.csrfTokenRepository(
                                        CookieCsrfTokenRepository.withHttpOnlyFalse()
                                ).csrfTokenRequestHandler(csrfTokenRequestAttributeHandler())
                                .ignoringRequestMatchers("/api/**")
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").authenticated()
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().permitAll()
                )
                .formLogin(f -> f.loginPage("/login")
                        .defaultSuccessUrl("/admin")
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .logoutSuccessUrl("/login")
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .addFilterBefore(exceptionHandlerFilter, LogoutFilter.class)
                .build();
    }

    @Bean
    public CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler() {
        var handler = new CsrfTokenRequestAttributeHandler();
        handler.setCsrfRequestAttributeName(null);
        return handler;
    }

    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        var authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider) {
        return authenticationProvider::authenticate;
    }
}
