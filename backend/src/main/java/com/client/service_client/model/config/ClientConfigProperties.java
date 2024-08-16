package com.client.service_client.model.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("jwt")
public record ClientConfigProperties(String secretKey, long expireTime, long refreshTime) { }
