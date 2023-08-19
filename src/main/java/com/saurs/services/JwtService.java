package com.saurs.services;

import com.saurs.models.TokenResponse;
import com.saurs.models.User;
import io.smallrye.jwt.algorithm.KeyEncryptionAlgorithm;
import io.smallrye.jwt.algorithm.SignatureAlgorithm;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.util.KeyUtils;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.hibernate.cfg.Environment;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;

@ApplicationScoped
public class JwtService {

    private static final String ISSUER = Environment.getProperties().getProperty("quarkus.smallrye-jwt.token.issuer");
    private SecretKey signingKey;
    private SecretKey encryptionKey;

    @PostConstruct
    public void init() {
        signingKey = generateSigningKey();
        encryptionKey = generateEncryptionKey();
    }

    public TokenResponse generateTokens(User user) {
        String accessToken = generateAccessToken(user, 20 * 60 * 1000L);
        String refreshToken = generateRefreshToken(user, 7 * 24 * 60 * 60 * 1000L);
        System.out.println(accessToken);
        // Get payload of token using Microprofile jwt
        return new TokenResponse(
                accessToken,
                refreshToken);
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
                .innerSign(signingKey)
                .encrypt(encryptionKey);
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
                .innerSign(signingKey)
                .encrypt(encryptionKey);
    }

    private SecretKey generateSigningKey() {
        try {
            return KeyUtils.generateSecretKey(SignatureAlgorithm.HS256);
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

    private SecretKey generateDecryptionKey() {
        try {
            return KeyUtils.generateSecretKey(KeyEncryptionAlgorithm.A256KW);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
