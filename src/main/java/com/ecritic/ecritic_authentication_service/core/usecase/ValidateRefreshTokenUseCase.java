package com.ecritic.ecritic_authentication_service.core.usecase;

import com.ecritic.ecritic_authentication_service.config.properties.ApplicationProperties;
import com.ecritic.ecritic_authentication_service.core.model.RefreshToken;
import com.ecritic.ecritic_authentication_service.core.model.Token;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.FindRefreshTokenByIdBoundary;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.ValidateJwtTokenBoundary;
import com.ecritic.ecritic_authentication_service.exception.UnauthorizedAccessException;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ValidateRefreshTokenUseCase {

    private final ValidateJwtTokenBoundary validateJwtTokenBoundary;

    private final FindRefreshTokenByIdBoundary findRefreshTokenByIdBoundary;

    private final ApplicationProperties applicationProperties;

    public RefreshToken execute(String jwtRefreshToken) {
        Token token = validateJwtTokenBoundary.execute(jwtRefreshToken, getSecretKey());

        log.info("Validating tokenId [{}] and userId: [{}]", token.getId(), token.getUser().getId());

        RefreshToken savedRefreshToken = getRefreshToken(token.getId());
        validateTokenFields(token, savedRefreshToken);

        return savedRefreshToken;
    }

    public SecretKey getSecretKey() {
        byte[] secretBytes = Base64.getDecoder().decode(applicationProperties.getJwtRefreshSecret());
        return new SecretKeySpec(secretBytes, "HmacSHA256");
    }

    private RefreshToken getRefreshToken(UUID id) {
        Optional<RefreshToken> optionalRefreshToken = findRefreshTokenByIdBoundary.execute(id);
        if (optionalRefreshToken.isEmpty()) {
            log.error("Refresh token with id [{}] not found", id);
            throw new UnauthorizedAccessException(ErrorResponseCode.ECRITICAUTH_03);
        }

        return optionalRefreshToken.get();
    }

    private void validateTokenFields(Token token, RefreshToken savedToken) {
        if (!savedToken.isActive()) {
            log.error("Refresh token with id [{}] and userId: [{}] is not active", savedToken.getId(), savedToken.getUser().getId());
            throw new UnauthorizedAccessException(ErrorResponseCode.ECRITICAUTH_03);
        }

        List<Boolean> conditions = Arrays.asList(
                token.getIssuer().equals(savedToken.getIssuer()),
                token.getAud().equals(savedToken.getAud()),
                token.getUser().getId().equals(savedToken.getUser().getId()),
                token.getIssuedAt().equals(savedToken.getIssuedAt().truncatedTo(ChronoUnit.SECONDS)),
                token.getExpiresAt().equals(savedToken.getExpiresAt().truncatedTo(ChronoUnit.SECONDS))
        );

        for (Boolean condition : conditions) {
            if (!condition) {
                log.error("Found violations while validating refresh token fields");
                throw new UnauthorizedAccessException(ErrorResponseCode.ECRITICAUTH_03);
            }
        }
    }
}
