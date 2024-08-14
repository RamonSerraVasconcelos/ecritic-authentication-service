package com.ecritic.ecritic_authentication_service.core.usecase;

import com.ecritic.ecritic_authentication_service.core.model.AccessToken;
import com.ecritic.ecritic_authentication_service.core.model.AuthenticationData;
import com.ecritic.ecritic_authentication_service.core.model.RefreshToken;
import com.ecritic.ecritic_authentication_service.core.model.User;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.FindUserBoundary;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.SaveRefreshTokenBoundary;
import com.ecritic.ecritic_authentication_service.exception.DefaultException;
import com.ecritic.ecritic_authentication_service.exception.UnauthorizedAccessException;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshUserTokenUseCase {

    private final ValidateRefreshTokenUseCase validateRefreshTokenUseCase;

    private final FindUserBoundary findUserBoundary;

    private final GenerateAccessTokenUseCase generateAccessTokenUseCase;

    private final GenerateRefreshTokenUseCase generateRefreshTokenUseCase;

    private final SaveRefreshTokenBoundary saveRefreshTokenBoundary;

    public AuthenticationData execute(String jwtRefreshToken) {
        log.info("Starting refresh user token process");

        try {
            RefreshToken savedRefreshToken = validateRefreshTokenUseCase.execute(jwtRefreshToken);

            User user = getUser(savedRefreshToken.getUser().getId());
            if (!user.isActive()) {
                log.error("User with id [{}] is not active", user.getId());
                throw new UnauthorizedAccessException(ErrorResponseCode.ECRITICAUTH_03);
            }

            AccessToken accessToken = generateAccessTokenUseCase.execute(user);
            RefreshToken refreshToken = generateRefreshTokenUseCase.execute(user);

            savedRefreshToken.setActive(false);
            saveRefreshTokenBoundary.execute(savedRefreshToken);

            return AuthenticationData.builder()
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

    private User getUser(UUID userId) {
        Optional<User> optionalUser = findUserBoundary.execute(null, userId);
        if (optionalUser.isEmpty()) {
            log.error("User with id [{}] not found", userId);
            throw new UnauthorizedAccessException(ErrorResponseCode.ECRITICAUTH_03);
        }

        return optionalUser.get();
    }
}
