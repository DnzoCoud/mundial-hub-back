package com.unbosque.mundial_hub;

import com.unbosque.mundial_hub.config.ExternalApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties(ExternalApiProperties.class)
@EnableJpaAuditing
public class MundialHubApplication {

	public static void main(String[] args) {
		SpringApplication.run(MundialHubApplication.class, args);
	}

}
