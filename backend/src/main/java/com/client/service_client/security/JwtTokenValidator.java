package com.client.service_client.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.client.service_client.model.config.ClientConfigProperties;

@Component
public class JwtTokenValidator {
    
    @Autowired
    private final ClientConfigProperties clientConfig;

    public JwtTokenValidator(ClientConfigProperties clientConfig) {
        this.clientConfig = clientConfig;
    }

    public String validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC512(clientConfig.secretKey().getBytes()))
                .build();

            DecodedJWT jwt = verifier.verify(token);
            return jwt.getSubject();
        }
        catch (JWTVerificationException e) {
            System.out.println(clientConfig);
            System.out.println(e.getMessage());

            return null;
        }
    }
}
