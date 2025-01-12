package com.client.service_client.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.client.service_client.controller.interfaces.IUserController;
import com.client.service_client.model.User;
import com.client.service_client.model.dto.PasswordDTO;
import com.client.service_client.model.dto.StatusDTO;
import com.client.service_client.model.dto.UserCreateDTO;
import com.client.service_client.model.dto.UserEditDTO;
import com.client.service_client.model.enums.UserStatus;
import com.client.service_client.model.record.UserList;
import com.client.service_client.model.response.ResponseOnlyMessage;
import com.client.service_client.model.response.ResponseWithData;
import com.client.service_client.model.response.ResponseWithInfo;
import com.client.service_client.service.UserService;

import jakarta.validation.Valid;

public class UserController implements IUserController {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseEntity<?> changeStatus(@Valid StatusDTO<UserStatus> entity) {
        try {
            if(entity.getStatus() == UserStatus.active || entity.getStatus() == UserStatus.inactive || entity.getStatus() == UserStatus.blocked) {
                userService.updateStatus(entity.getId(), entity.getStatus());
            }
            else {
                throw new IllegalArgumentException("Status is not valid");
            }
            
            return ResponseEntity.ok().body(new ResponseOnlyMessage("Request successful"));
        }
        catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> createUser(@Valid UserCreateDTO entity) {
        try {
            User user = new User();

            user.setEmail(entity.getEmail());
            user.setPassword(entity.getPassword());
            user.setUsername(entity.getUsername());
            user.setFirst_name(entity.getFirst_name());
            user.setLast_name(entity.getLast_name());

            userService.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseOnlyMessage("New user created"));
        }
        catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> editUser(@Valid UserEditDTO entity) {
        try {
            userService.editUser(entity);
            return ResponseEntity.ok().body(new ResponseOnlyMessage("User updated"));
        }
        catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> getAllUser() {
        try {
            List<UserList> users = userService.getAllUser();

            if(users.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok().body(new ResponseWithData<List<UserList>>("Request successful", users));
            
        }
        catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> changePassword(@Valid PasswordDTO entity) {
        try {
            String password = userService.findPasswordById(entity.getId());

            if(!bCryptPasswordEncoder.matches(password, entity.getPassword())) {
                throw new IllegalArgumentException("The password is the same");
            }

            userService.updatePassword(entity.getId(), passwordEncoder.encode(entity.getPassword()));
            return ResponseEntity.ok().body(new ResponseOnlyMessage("Password updated"));
        }
        catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> getUserById(String id) {
        try {
            Optional<User> user = userService.findUserById(id);

            if(!user.isPresent()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok().body(new ResponseWithData<User>("Request successful", user.get()));

        }
        catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        }
    }

    
    
}
