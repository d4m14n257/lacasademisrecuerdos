package com.client.service_client.controller.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.client.service_client.model.dto.PasswordDTO;
import com.client.service_client.model.dto.StatusDTO;
import com.client.service_client.model.dto.UserCreateDTO;
import com.client.service_client.model.dto.UserEditDTO;
import com.client.service_client.model.enums.UserStatus;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RequestMapping("/api/user")
public interface IUserController {

    @GetMapping
    public ResponseEntity<?> getAllUser();

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id);

    @PostMapping
    @Transactional
    public ResponseEntity<?> createUser(@Valid @RequestBody UserCreateDTO entity);

    @PutMapping
    @Transactional
    public ResponseEntity<?> editUser(@Valid @RequestBody UserEditDTO entity);

    @PutMapping("/status")
    @Transactional
    public ResponseEntity<?> changeStatus(@Valid @RequestBody StatusDTO<UserStatus> entity);

    @PutMapping("/password")
    @Transactional
    public ResponseEntity<?> changePassword(@RequestBody PasswordDTO entity);
}
