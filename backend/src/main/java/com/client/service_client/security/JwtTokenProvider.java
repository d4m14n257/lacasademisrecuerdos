package com.client.service_client.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.client.service_client.model.config.ClientConfigProperties;

@Component
public class JwtTokenProvider {
    @Autowired
    private final ClientConfigProperties clientConfig;

    public JwtTokenProvider(ClientConfigProperties clientConfig) {
        this.clientConfig = clientConfig;
    }

    public String generateToken(String username) {
        return JWT.create()
            .withSubject(username)
            .withIssuedAt(new Date())
            .withExpiresAt(new Date(System.currentTimeMillis() + clientConfig.expirationTime()))
            .sign(Algorithm.HMAC512(clientConfig.secretKey().getBytes()));
    }
}
