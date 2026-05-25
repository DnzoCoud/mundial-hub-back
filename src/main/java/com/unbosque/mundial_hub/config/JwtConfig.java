package com.unbosque.mundial_hub.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "jwt")
record JwtProperties(
    String secret
) {
    public JwtProperties {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException(
                "JWT_SECRET environment variable is missing"
            );
        }
    }
}

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
@RequiredArgsConstructor
public class JwtConfig {
    private final JwtProperties jwtProperties;

    public String getSecret() {
        return jwtProperties.secret();
    }
}