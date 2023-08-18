package com.saurs.services;

import com.saurs.models.TokenResponse;
import com.saurs.models.User;
import io.smallrye.jwt.algorithm.KeyEncryptionAlgorithm;
import io.smallrye.jwt.algorithm.SignatureAlgorithm;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.util.KeyUtils;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;

@ApplicationScoped
public class JwtService {

    @ConfigProperty(name = "quarkus.smallrye-jwt.token.issuer")
    private String ISSUER;

    public TokenResponse generateTokens(User user) {
        return new TokenResponse(
                generateAccessToken(user, 20 * 60 * 1000L),
                generateRefreshToken(user, 7 * 24 * 60 * 60 * 1000L));
    }

    private String generateAccessToken(User user, Long expirationTime) {
        return Jwt
                .issuer(ISSUER)
                .upn(user.getEmail())
                .claim("uid", user.getId())
                .claim("roles", user.getRoles())
                .issuedAt(System.currentTimeMillis())
                .expiresIn(expirationTime)
                .claim("refresh", false)
                .innerSign(generateSigningKey())
                .encrypt(generateEncryptionKey());
    }

    private String generateRefreshToken(User user, Long expirationTime) {
        return Jwt
                .issuer(ISSUER)
                .upn(user.getEmail())
                .claim("uid", user.getEmail())
                .claim("roles", user.getRoles())
                .issuedAt(System.currentTimeMillis())
                .expiresIn(expirationTime)
                .claim("refresh", true)
                .innerSign(generateSigningKey())
                .encrypt(generateEncryptionKey());
    }

    private SecretKey generateSigningKey() {
        try {
            return KeyUtils.generateSecretKey(SignatureAlgorithm.HS512);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private SecretKey generateEncryptionKey() {
        try {
            return KeyUtils.generateSecretKey(KeyEncryptionAlgorithm.A256KW);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
