package com.client.service_client.model.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.mail")
public record MailConfigProperties(String username) { }
