package com.unbosque.mundial_hub.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {
    private final ExternalApiProperties externalApiProperties;

    @Bean
    public WebClient apiFootballClient() {
        return WebClient.builder()
                .baseUrl("https://v3.football.api-sports.io")
                .defaultHeader("x-apisports-key", externalApiProperties.getApiFootball())
                .build();
    }
}
