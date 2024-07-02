package com.ecritic.ecritic_authentication_service.core.usecase;

import com.ecritic.ecritic_authentication_service.config.properties.ApplicationProperties;
import com.ecritic.ecritic_authentication_service.core.model.AccessToken;
import com.ecritic.ecritic_authentication_service.core.model.AuthorizationData;
import com.ecritic.ecritic_authentication_service.core.model.RefreshToken;
import com.ecritic.ecritic_authentication_service.core.model.Token;
import com.ecritic.ecritic_authentication_service.core.model.User;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.FindRefreshTokenByIdBoundary;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.FindUserBoundary;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.ValidateJwtTokenBoundary;
import com.ecritic.ecritic_authentication_service.exception.DefaultException;
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
public class RefreshUserTokenUseCase {

    private final ValidateJwtTokenBoundary validateJwtTokenBoundary;

    private final FindRefreshTokenByIdBoundary findRefreshTokenByIdBoundary;

    private final FindUserBoundary findUserBoundary;

    private final GenerateAccessTokenUseCase generateAccessTokenUseCase;

    private final GenerateRefreshTokenUseCase generateRefreshTokenUseCase;

    private final ApplicationProperties applicationProperties;

    public AuthorizationData execute(String jwtRefreshToken) {
        log.info("Starting refresh user token process");

        try {
            Token token = validateJwtTokenBoundary.execute(jwtRefreshToken, getSecretKey());

            log.info("Refreshing tokenId [{}] and userId: [{}]", token.getId(), token.getUser().getId());

            RefreshToken savedRefreshToken = getRefreshToken(token.getId());
            validateTokenFields(token, savedRefreshToken);

            User user = getUser(savedRefreshToken.getUser().getId());
            if (!user.isActive()) {
                log.error("User with id [{}] is not active", user.getId());
                throw new UnauthorizedAccessException(ErrorResponseCode.ECRITICAUTH_03);
            }

            AccessToken accessToken = generateAccessTokenUseCase.execute(user);
            RefreshToken refreshToken = generateRefreshTokenUseCase.execute(user);

            return AuthorizationData.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (DefaultException ex) {
            log.error("Error refreshing user token. Exception: [{}]", ex.getErrorResponse());
            throw ex;
        } catch (Exception ex) {
            log.error("Error refreshing user token", ex);
            throw new UnauthorizedAccessException(ErrorResponseCode.ECRITICAUTH_03);
        }
    }

    public SecretKey getSecretKey() {
        byte[] secretBytes = Base64.getDecoder().decode(applicationProperties.getJwtRefreshSecret());
        return new SecretKeySpec(secretBytes, "HmacSHA256");
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

    private RefreshToken getRefreshToken(UUID id) {
        Optional<RefreshToken> optionalRefreshToken = findRefreshTokenByIdBoundary.execute(id);
        if (optionalRefreshToken.isEmpty()) {
            log.error("Refresh token with id [{}] not found", id);
            throw new UnauthorizedAccessException(ErrorResponseCode.ECRITICAUTH_03);
        }

        return optionalRefreshToken.get();
    }

    private User getUser(UUID userId) {
        Optional<User> optionalUser = findUserBoundary.execute(null, userId);
        if (optionalUser.isEmpty()) {
            log.error("User with id [{}] not found", userId);
            throw new UnauthorizedAccessException(ErrorResponseCode.ECRITICAUTH_03);
        }

        return optionalUser.get();
    }
}
