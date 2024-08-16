package com.client.service_client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.client.service_client.model.config.ClientConfigProperties;

@SpringBootApplication
@EnableConfigurationProperties(ClientConfigProperties.class)
public class ServiceClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceClientApplication.class, args);
	}

}
