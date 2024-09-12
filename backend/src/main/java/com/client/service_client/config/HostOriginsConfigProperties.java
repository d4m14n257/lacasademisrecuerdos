package com.client.service_client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("host")
public record HostOriginsConfigProperties (String serverAdmin, String serverClient) { }
