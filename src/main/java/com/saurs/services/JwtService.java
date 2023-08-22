package com.saurs.services;

import com.saurs.exceptions.ResourceNotFoundException;
import com.saurs.models.RefreshToken;
import com.saurs.models.TokenResponse;
import com.saurs.models.User;
import io.smallrye.jwt.algorithm.KeyEncryptionAlgorithm;
import io.smallrye.jwt.algorithm.SignatureAlgorithm;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import io.smallrye.jwt.build.JwtException;
import io.smallrye.jwt.util.KeyUtils;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.impl.jose.JWT;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.hibernate.cfg.Environment;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;

import javax.crypto.SecretKey;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.util.Base64;
import java.util.Optional;

@ApplicationScoped
public class JwtService {

    @Inject
    UriInfo uriInfo;

    private SecretKey signingKey;

    @PostConstruct
    public void init() {
        signingKey = generateSigningKey();
    }

    public TokenResponse generateTokens(User user) {
        String accessToken = generateAccessToken(user, 20 * 60 * 1000L);
        String refreshToken = generateRefreshToken(user, 7 * 24 * 60 * 60 * 1000L);
        return new TokenResponse(
                accessToken,
                refreshToken);
    }

    public TokenResponse refreshTokens(RefreshToken token) {
        if (!isValidRefreshToken(token.refreshToken())) {
            throw new JwtException("Invalid refresh token");
        }
        User user = extractUserFromToken(token.refreshToken());
        String accessToken = generateAccessToken(user, 20 * 60 * 1000L);
        String refreshToken = generateRefreshToken(user, 7 * 24 * 60 * 60 * 1000L);
        return new TokenResponse(
                accessToken,
                refreshToken);
    }

    private String generateAccessToken(User user, Long expirationTime) {
        String issuer = uriInfo.getBaseUri().getHost();

        return Jwt
                .issuer(issuer)
                .subject(user.email)
                .claim("uid", user.id)
                .claim("roles", user.roles.stream().map(role -> role.name).toList())
                .issuedAt(System.currentTimeMillis())
                .expiresIn(expirationTime)
                .claim("refresh", false)
                .sign(signingKey);
    }

    private String generateRefreshToken(User user, Long expirationTime) {
        String issuer = uriInfo.getBaseUri().getHost();

        return Jwt
                .issuer(issuer)
                .subject(user.email)
                .issuedAt(System.currentTimeMillis())
                .expiresIn(expirationTime)
                .claim("refresh", true)
                .sign(signingKey);
    }

    private boolean isValidRefreshToken(String token) {
        try {
            var claims = JwtClaims.parse(String.valueOf(JWT.parse(token).getValue("payload")));
            var expired = claims.getExpirationTime().getValueInMillis() < System.currentTimeMillis();
            var refresh = claims.getClaimValue("refresh");
            if (refresh instanceof Boolean isRefresh && !expired) {
                return isRefresh;
            } else {
                return false;
            }
        } catch (InvalidJwtException | MalformedClaimException e) {
            return false;
        }
    }

    public User validateToken(String token) {
        try {
            var claims = JwtClaims.parse(String.valueOf(JWT.parse(token).getValue("payload")));
            var expired = claims.getExpirationTime().getValueInMillis() < System.currentTimeMillis();
            var email = claims.getSubject();
            if (expired) {
                throw new JwtException("Token expired");
            }
            if (email != null) {
                Optional<User> user = User.find("email", email).firstResultOptional();
                return user.orElseThrow(() -> new ResourceNotFoundException(email));
            } else {
                throw new JwtException("Invalid token");
            }
        } catch (InvalidJwtException | MalformedClaimException e) {
            throw new JwtException(e.getMessage());
        }
    }

    private User extractUserFromToken(String token) {
        try {
            var claims = JwtClaims.parse(String.valueOf(JWT.parse(token).getValue("payload")));
            var email = claims.getSubject();
            if (email != null) {
               Optional<User> user = User.find("email", email).firstResultOptional();
               return user.orElseThrow(() -> new ResourceNotFoundException(email));
            } else {
                throw new JwtException("Invalid token");
            }
        } catch (InvalidJwtException | MalformedClaimException e) {
            throw new JwtException("Invalid token");
        }
    }

    private SecretKey generateSigningKey() {
        try {
            return KeyUtils.generateSecretKey(SignatureAlgorithm.HS256);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }
}
