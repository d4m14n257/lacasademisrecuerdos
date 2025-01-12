package com.client.service_client.model;

import java.time.LocalDateTime;

import com.client.service_client.model.enums.UserStatus;
import com.client.service_client.util.CustomIdGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "User")
public class User {

    @Id
    @Column(name = "id", length = 12)
    private String id;

    @Column(name = "email", length = 128)
    private String email;

    @Column(name = "username", length = 128)
    private String username;

    @Column(name = "password", length = 64)
    private String password;

    @Column(name = "first_name", length = 128)
    private String first_name;

    @Column(name = "last_name", length = 128)
    private String last_name;
    
    @Column(name = "created_at")
    private LocalDateTime created_at;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    public User() {
        this.id = CustomIdGenerator.generate(12);
        this.status = UserStatus.inactive;
        this.created_at = LocalDateTime.now();
    }

    public User(String id) {
        this.id = id;
        this.status = UserStatus.inactive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}
