package com.client.service_client;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.client.service_client.model.config.ClientConfigProperties;
import com.client.service_client.storage.StorageProperties;
import com.client.service_client.storage.StorageService;

@SpringBootApplication
@EnableConfigurationProperties({ClientConfigProperties.class, StorageProperties.class})
public class ServiceClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceClientApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.deleteAll();
			storageService.init();
		};
	}
}
