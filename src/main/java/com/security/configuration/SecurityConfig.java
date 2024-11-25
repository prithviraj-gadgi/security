package com.security.configuration;

import com.security.exception.InvalidUserAuthenticationEntryPoint;
import com.security.filter.SecurityFilter;
import com.security.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final InvalidUserAuthenticationEntryPoint authenticationEntryPoint;
    private final SecurityFilter securityFilter;

    @Autowired
    public SecurityConfig(
            JWTService jwtService,
            PasswordEncoder passwordEncoder,
            InvalidUserAuthenticationEntryPoint authenticationEntryPoint,
            SecurityFilter securityFilter) {
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.securityFilter = securityFilter;
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(
                                "/user/save",
                                "/user/login",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/actuator/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .exceptionHandling(
                        exceptionHandling -> exceptionHandling.authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(
                        sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(jwtService).passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }
}
