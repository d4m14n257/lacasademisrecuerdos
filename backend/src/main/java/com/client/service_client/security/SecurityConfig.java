package com.client.service_client.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.client.service_client.config.HostOriginsConfigProperties;
import com.client.service_client.model.record.Endpoints;
import com.client.service_client.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private HostOriginsConfigProperties hostOriginsConfigProperties;
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private UserService userService;
    private JwtTokenValidator jwtTokenValidator;
    private final List<Endpoints> endpointsPermited = new ArrayList<Endpoints>(
        Arrays.asList(
            new Endpoints(HttpMethod.GET, "/api/room"), 
            new Endpoints(HttpMethod.GET, "/api/room/list"),
            new Endpoints(HttpMethod.GET, "/api/room/client/{id}"),
            new Endpoints(HttpMethod.GET, "/api/hotel"),
            new Endpoints(HttpMethod.GET, "/api/tour"),
            new Endpoints(HttpMethod.GET, "/api/tour/{id}"),
            new Endpoints(HttpMethod.POST, "/api/contact"))
    );
    private final List<Endpoints> endpointsAuthenticated = new ArrayList<Endpoints>(
        Arrays.asList(
            new Endpoints(HttpMethod.GET, "/api/room/admin"), 
            new Endpoints(HttpMethod.GET, "/api/room/admin/{id}"), 
            new Endpoints(HttpMethod.GET, "/api/contact/admin"),
            new Endpoints(HttpMethod.GET, "/api/tour/admin{id}"),
            new Endpoints(HttpMethod.POST, "/api/room/admin"),
            new Endpoints(HttpMethod.POST, "/api/tour/admin"),
            new Endpoints(HttpMethod.POST, "/api/hotel/admin"),
            new Endpoints(HttpMethod.POST, "/api/file/admin/{name}"),
            new Endpoints(HttpMethod.PUT, "/api/room/admin"),
            new Endpoints(HttpMethod.PUT, "/api/room/admin/status"),
            new Endpoints(HttpMethod.PUT, "/api/file/admin/add/{name}"),
            new Endpoints(HttpMethod.PUT, "/api/contact/admin"),
            new Endpoints(HttpMethod.PUT, "/api/hotel/admin"),
            new Endpoints(HttpMethod.PUT, "/api/tour/admin"),
            new Endpoints(HttpMethod.DELETE, "/api/delete/admin/{name}"),
            new Endpoints(HttpMethod.DELETE, "/api/file/admin")
        ));

    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, UserService userService, JwtTokenValidator jwtTokenValidator, HostOriginsConfigProperties hostOriginsConfigProperties) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtTokenValidator = jwtTokenValidator;
        this.userService = userService;
        this.hostOriginsConfigProperties = hostOriginsConfigProperties;
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
    @Order(0)
    SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
        return http
            .cors(cors -> cors.configurationSource(adminCorsFilter()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(handling -> 
                handling.authenticationEntryPoint(jwtAuthenticationEntryPoint))
            .authorizeHttpRequests(req -> {
                req.requestMatchers("/api/auth/**").permitAll();
                for (Endpoints authenticated : endpointsAuthenticated) { req.requestMatchers(authenticated.method(), authenticated.url()).authenticated(); }
                req.anyRequest().authenticated();
            })
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .build();       
    }

    @Bean
    @Order(1)
    SecurityFilterChain clientFilterChain(HttpSecurity http) throws Exception {
        return http
            .cors(cors -> cors.configurationSource(clientCorsFilter()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(handling -> 
                handling.authenticationEntryPoint(jwtAuthenticationEntryPoint))
            .authorizeHttpRequests(req -> {
                for (Endpoints permited : endpointsPermited) { req.requestMatchers(permited.method(), permited.url()).permitAll(); }
                req.anyRequest().authenticated();
            })
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .build(); 
    }   

    @Bean
    CorsConfigurationSource adminCorsFilter() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowedOrigins(Arrays.asList(hostOriginsConfigProperties.serverAdmin()));
        cors.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        cors.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
        cors.setExposedHeaders(Arrays.asList("Authorization"));
        cors.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);

        return source;
    }

    @Bean
    CorsConfigurationSource clientCorsFilter() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowedOrigins(Arrays.asList(hostOriginsConfigProperties.serverClient()));
        cors.setAllowedMethods(Arrays.asList("GET", "POST"));
        cors.setAllowedHeaders(Arrays.asList("Content-Type", "Accept"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        
        return source;
    }


}
