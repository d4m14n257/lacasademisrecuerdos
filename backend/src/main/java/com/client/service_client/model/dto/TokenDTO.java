package com.client.service_client.model.dto;

import jakarta.validation.constraints.NotBlank;

public class TokenDTO {
    
    @NotBlank(message = "Token is require")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
