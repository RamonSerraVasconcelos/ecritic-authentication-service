package com.ecritic.ecritic_authentication_service.core.usecase;

import com.ecritic.ecritic_authentication_service.config.properties.ApplicationProperties;
import com.ecritic.ecritic_authentication_service.core.model.RefreshToken;
import com.ecritic.ecritic_authentication_service.core.model.User;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.GenerateJwtTokenBoundary;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.SaveRefreshTokenBoundary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenerateRefreshTokenUseCase {

    private final GenerateJwtTokenBoundary generateJwtTokenBoundary;

    private final SaveRefreshTokenBoundary saveRefreshTokenBoundary;

    private final ApplicationProperties applicationProperties;

    public RefreshToken execute(User user) {
        log.info("Generating refresh token for userId: [{}]", user.getId());

        try {
            Set<String> auds = new HashSet<>();
            auds.add("ecritic");

            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setId(UUID.randomUUID());
            refreshToken.setAud(auds);
            refreshToken.setIssuer("ecritic");
            refreshToken.setUser(user);
            refreshToken.setActive(true);
            refreshToken.setIssuedAt(LocalDateTime.now());
            refreshToken.setExpiresAt(LocalDateTime.now().plusSeconds(applicationProperties.getJwtRefreshExpiration()));

            String jwtToken = generateJwtTokenBoundary.execute(refreshToken, getSecretKey());
            refreshToken.setJwt(jwtToken);

            saveRefreshTokenBoundary.execute(refreshToken);

            return refreshToken;
        } catch (Exception ex) {
            log.error("Error generating refreshToken for userId: [{}]", user.getId(), ex);
            throw ex;
        }
    }

    public SecretKey getSecretKey() {
        byte[] secretBytes = Base64.getDecoder().decode(applicationProperties.getJwtRefreshSecret());
        return new SecretKeySpec(secretBytes, "HmacSHA256");
    }
}
