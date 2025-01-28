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
            new Endpoints(HttpMethod.GET, "/client/room"), 
            new Endpoints(HttpMethod.GET, "/client/room/{id}"), 
            new Endpoints(HttpMethod.GET, "/client/room/list"),
            new Endpoints(HttpMethod.GET, "/client/room/client/{id}"),
            new Endpoints(HttpMethod.GET, "/client/hotel"),
            new Endpoints(HttpMethod.GET, "/client/tour"),
            new Endpoints(HttpMethod.GET, "/client/tour/{id}"),
            new Endpoints(HttpMethod.POST, "/client/contact"))
    );
    private final List<Endpoints> endpointsAuthenticated = new ArrayList<Endpoints>(
        Arrays.asList(
            new Endpoints(HttpMethod.GET, "/admin/room"), 
            new Endpoints(HttpMethod.GET, "/admin/room/{id}"), 
            new Endpoints(HttpMethod.GET, "/admin/contact"),
            new Endpoints(HttpMethod.GET, "/admin/tour{id}"),
            new Endpoints(HttpMethod.POST, "/admin/room"),
            new Endpoints(HttpMethod.POST, "/admin/tour"),
            new Endpoints(HttpMethod.POST, "/admin/hotel"),
            new Endpoints(HttpMethod.POST, "/admin/file/{name}"),
            new Endpoints(HttpMethod.PUT, "/admin/room"),
            new Endpoints(HttpMethod.PUT, "/admin/room/status"),
            new Endpoints(HttpMethod.PUT, "/admin/file/add/{name}"),
            new Endpoints(HttpMethod.PUT, "/admin/file/main/{name}"),
            new Endpoints(HttpMethod.PUT, "/admin/contact"),
            new Endpoints(HttpMethod.PUT, "/admin/hotel"),
            new Endpoints(HttpMethod.PUT, "/admin/tour"),
            new Endpoints(HttpMethod.DELETE, "/admin/delete/{name}"),
            new Endpoints(HttpMethod.DELETE, "/admin/file")
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
    @Order(2)
    SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
        return http
            .cors(cors -> cors.configurationSource(adminCorsFilter()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(handling -> 
                handling.authenticationEntryPoint(jwtAuthenticationEntryPoint))
            .securityMatcher("/admin/**")
            .authorizeHttpRequests(req -> {
                for (Endpoints authenticated : endpointsAuthenticated) { req.requestMatchers(authenticated.method(), authenticated.url()).authenticated(); }
                req.anyRequest().authenticated();
            })
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .build();       
    }

    @Bean
    @Order(1)
    SecurityFilterChain loginFilterChain(HttpSecurity http) throws Exception {
        return http
            .cors(cors -> cors.configurationSource(adminCorsFilter()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .securityMatcher("/auth/**")
            .authorizeHttpRequests(req -> {
                req.requestMatchers("/auth/**").permitAll();
            })
            .build();
    }

    @Bean
    SecurityFilterChain clientFilterChain(HttpSecurity http) throws Exception {
        return http
            .cors(cors -> cors.configurationSource(clientCorsFilter()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .securityMatcher("/client/**")
            .authorizeHttpRequests(req -> {
                for (Endpoints permited : endpointsPermited) { req.requestMatchers(permited.method(), permited.url()).permitAll(); }
            })
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
