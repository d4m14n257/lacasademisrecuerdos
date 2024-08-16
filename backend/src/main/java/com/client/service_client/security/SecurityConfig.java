package com.client.service_client.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.client.service_client.model.record.Endpoints;
import com.client.service_client.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private UserService userService;
    private JwtTokenValidator jwtTokenValidator;
    private final List<Endpoints> endpointsPermited = new ArrayList<Endpoints>(
        Arrays.asList(
            new Endpoints(HttpMethod.GET, "/api/room"), 
            new Endpoints(HttpMethod.GET, "/api/room/list"),
            new Endpoints(HttpMethod.GET, "/api/room/{id}"),
            new Endpoints(HttpMethod.GET, "/api/hotel"),
            new Endpoints(HttpMethod.GET, "/api/tour"),
            new Endpoints(HttpMethod.GET, "/api/tour/{id}"),
            new Endpoints(HttpMethod.POST, "/api/contact"))
    );
    private final List<Endpoints> endpointsAuthenticated = new ArrayList<Endpoints>(
        Arrays.asList(
            new Endpoints(HttpMethod.GET, "/api/room/admin/{id}"), 
            new Endpoints(HttpMethod.GET, "/api/contact/admin"),
            new Endpoints(HttpMethod.GET, "/api/tour/admin{id}"),
            new Endpoints(HttpMethod.POST, "/api/room/admin"),
            new Endpoints(HttpMethod.POST, "/api/tour/admin"),
            new Endpoints(HttpMethod.POST, "/api/hotel/admin"),
            new Endpoints(HttpMethod.PUT, "/api/room/admin"),
            new Endpoints(HttpMethod.PUT, "/api/contact/admin"),
            new Endpoints(HttpMethod.PUT, "/api/hotel/admin"),
            new Endpoints(HttpMethod.PUT, "/api/tour/admin"),
            new Endpoints(HttpMethod.DELETE, "/api/room/admin"),
            new Endpoints(HttpMethod.DELETE, "/api/contact/admin"),
            new Endpoints(HttpMethod.DELETE, "/api/hotel/admin"),
            new Endpoints(HttpMethod.DELETE, "/api/tour/admin"))
    );

    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, UserService userService, JwtTokenValidator jwtTokenValidator) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtTokenValidator = jwtTokenValidator;
        this.userService = userService;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtAuthorizationFilter jwtAuthenticationFilter() {
        return new JwtAuthorizationFilter(userService, jwtTokenValidator);
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(handling -> 
                handling.authenticationEntryPoint(jwtAuthenticationEntryPoint))
            .authorizeHttpRequests(req -> {
                req.requestMatchers("/api/auth/**").permitAll();
                for (Endpoints permited : endpointsPermited) { req.requestMatchers(permited.method(), permited.url()).permitAll(); }
                for (Endpoints authenticated : endpointsAuthenticated) { req.requestMatchers(authenticated.method(), authenticated.url()).authenticated(); }
                req.anyRequest().authenticated();
            })
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .build();       
    }

}
