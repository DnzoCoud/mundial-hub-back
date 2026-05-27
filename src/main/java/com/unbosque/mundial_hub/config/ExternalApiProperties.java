package com.unbosque.mundial_hub.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "external-api-keys")
@Getter
@Setter
public class ExternalApiProperties {
    private String apiFootball;
}
