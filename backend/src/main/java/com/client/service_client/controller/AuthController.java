package com.client.service_client.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.client.service_client.config.ClientConfigProperties;
import com.client.service_client.model.User;
import com.client.service_client.model.dto.LoginDTO;
import com.client.service_client.model.dto.UserDTO;
import com.client.service_client.model.record.JwtResponse;
import com.client.service_client.model.record.UserResponse;
import com.client.service_client.model.response.ResponseOnlyMessage;
import com.client.service_client.model.response.ResponseWithData;
import com.client.service_client.model.response.ResponseWithInfo;
import com.client.service_client.security.JwtTokenProvider;
import com.client.service_client.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;
    private UserService userService;
    private ClientConfigProperties clientProperties;

    public AuthController(
            AuthenticationManager authenticationManager, 
            PasswordEncoder passwordEncoder, 
            JwtTokenProvider jwtTokenProvider, 
            UserService userService, 
            ClientConfigProperties clientProperties
        ) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.clientProperties = clientProperties;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO entity) {
        try {
            if(userService.existsUser(entity.getUsername(), entity.getEmail())) {
                return ResponseEntity.badRequest().body(new ResponseOnlyMessage("Username or email exists, retry with other"));
            }

            User user = new User();
            user.setFirst_name(entity.getFirst_name());
            user.setLast_name(entity.getLast_name());
            user.setUsername(entity.getUsername());
            user.setEmail(entity.getEmail());
            user.setPassword(passwordEncoder.encode(entity.getPassword()));
            
            userService.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseOnlyMessage("New user created"));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO entity) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(entity.getUsername(), entity.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            String token = jwtTokenProvider.generateToken(entity.getUsername());
            User user = userService.findUserByUsername(entity.getUsername());

            UserResponse userResponse = new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getFirst_name(), user.getLast_name());
            JwtResponse jwtResponse = new JwtResponse(userResponse, token, "Bearer", clientProperties.expirationTime());
            return ResponseEntity.ok().body(new ResponseWithData<JwtResponse>("Request successful", jwtResponse));
        }
        catch(BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseOnlyMessage(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        }
    }    
}
