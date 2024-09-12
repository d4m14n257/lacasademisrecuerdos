package com.client.service_client;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.client.service_client.config.ClientConfigProperties;
import com.client.service_client.config.HostOriginsConfigProperties;
import com.client.service_client.config.MailConfigProperties;
import com.client.service_client.storage.StorageProperties;
import com.client.service_client.storage.StorageService;

@SpringBootApplication
@EnableConfigurationProperties({ClientConfigProperties.class, StorageProperties.class, MailConfigProperties.class, HostOriginsConfigProperties.class})
public class ServiceClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceClientApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.init();
		};
	}
}
