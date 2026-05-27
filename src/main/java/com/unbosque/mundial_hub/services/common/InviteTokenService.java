package com.unbosque.mundial_hub.services.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

@Service
public class InviteTokenService {
    private static final SecureRandom RANDOM = new SecureRandom();
    @Value("${app.invite.secret}")
    private String secret;

    public String generateInviteToken(UUID entityId) {
        try {

            String payload = entityId + ":" + System.currentTimeMillis() + ":" + RANDOM.nextLong();

            Mac mac = Mac.getInstance("HmacSHA256");

            SecretKeySpec secretKey = new SecretKeySpec(
                    secret.getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256"
            );

            mac.init(secretKey);

            byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));

            return Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(hash);

        } catch (Exception e) {
            throw new RuntimeException("Error generating invite token", e);
        }
    }
}
