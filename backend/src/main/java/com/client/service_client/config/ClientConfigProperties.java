package com.client.service_client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("jwt")
public record ClientConfigProperties(String secretKey, long expirationTime, long refreshTime) { }
