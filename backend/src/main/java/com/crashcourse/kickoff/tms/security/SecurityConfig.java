package com.crashcourse.kickoff.tms.security;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.crashcourse.kickoff.tms.user.Role;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userSvc) {
        this.userDetailsService = userSvc;
    }

    public static Set<Role> getAllRolesAsSet() {
        return Arrays.stream(Role.values())
                .collect(Collectors.toSet());
    }

    /**
     * Exposes a bean of DaoAuthenticationProvider, a type of AuthenticationProvider
     * Attaches the user details and the password encoder
     * 
     * @return
     */

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());

        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers(HttpMethod.GET, "/users/**").hasRole(Role.ROLE_ADMIN.name().substring(5))
                        .anyRequest().permitAll()
                        )
                // a bunch of copy pasted code from lab, need to sieve out irrelevant functions
                // ensure that the application won’t create any session in our stateless REST
                // APIs
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable()) // CSRF protection is needed only for browser based attacks
                .formLogin(form -> form.disable())
                .headers(header -> header.disable()) // disable the security headers, as we do not return HTML in our
                                                     // APIs
                .authenticationProvider(authenticationProvider());
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
