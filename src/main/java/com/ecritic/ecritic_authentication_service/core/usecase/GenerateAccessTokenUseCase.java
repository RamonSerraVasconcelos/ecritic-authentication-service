package com.ecritic.ecritic_authentication_service.core.usecase;

import com.ecritic.ecritic_authentication_service.config.properties.ApplicationProperties;
import com.ecritic.ecritic_authentication_service.core.model.AccessToken;
import com.ecritic.ecritic_authentication_service.core.model.User;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.GenerateJwtTokenBoundary;
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
public class GenerateAccessTokenUseCase {

    private final GenerateJwtTokenBoundary generateJwtTokenBoundary;

    private final ApplicationProperties applicationProperties;

    public AccessToken execute(User user) {
        log.info("Generating access token for userId: [{}]", user.getId());

        try {
            Set<String> auds = new HashSet<>();
            auds.add("ecritic");

            AccessToken accessToken = new AccessToken();
            accessToken.setId(UUID.randomUUID());
            accessToken.setAud(auds);
            accessToken.setIssuer("ecritic");
            accessToken.setUser(user);
            accessToken.setIssuedAt(LocalDateTime.now());
            accessToken.setExpiresAt(LocalDateTime.now().plusSeconds(applicationProperties.getJwtExpiration()));

            String jwtToken = generateJwtTokenBoundary.execute(accessToken, getSecretKey());
            accessToken.setJwt(jwtToken);

            return accessToken;
        } catch (Exception ex) {
            log.error("Error generating accessToken for userId: [{}]", user.getId(), ex);
            throw ex;
        }
    }

    public SecretKey getSecretKey() {
        byte[] secretBytes = Base64.getDecoder().decode(applicationProperties.getJwtSecret());
        return new SecretKeySpec(secretBytes, "HmacSHA256");
    }
}
