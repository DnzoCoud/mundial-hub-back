package com.unbosque.mundial_hub.services.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.unbosque.mundial_hub.beans.JwtConfig;
import com.unbosque.mundial_hub.models.UserEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.io.Decoders;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtService {
    private final JwtConfig jwtProperties;

    public String getSecret() {
        return jwtProperties.getSecret();
    }

    public String generateToken(UserEntity user) throws IllegalArgumentException, JWTCreationException {
        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("role", user.getRole().name())
                .withIssuedAt(new Date())
                .withIssuer("MUNDIAL-HUB APPLICATION")
                .sign(Algorithm.HMAC256(jwtProperties.getSecret()));
    }

    public String validateTokenAndRetriveSubject(String token)
            throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(this.getSecret()))
                .withIssuer("MUNDIAL-HUB APPLICATION")
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getSubject();
    }
}
