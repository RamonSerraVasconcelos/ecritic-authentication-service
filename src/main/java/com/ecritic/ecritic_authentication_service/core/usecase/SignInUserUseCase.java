package com.ecritic.ecritic_authentication_service.core.usecase;

import com.ecritic.ecritic_authentication_service.core.model.AccessToken;
import com.ecritic.ecritic_authentication_service.core.model.AuthenticationData;
import com.ecritic.ecritic_authentication_service.core.model.RefreshToken;
import com.ecritic.ecritic_authentication_service.core.model.User;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.FindUserBoundary;
import com.ecritic.ecritic_authentication_service.exception.BusinessViolationException;
import com.ecritic.ecritic_authentication_service.exception.DefaultException;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignInUserUseCase {

    private final FindUserBoundary findUserBoundary;

    private final GenerateAccessTokenUseCase generateAccessTokenUseCase;

    private final GenerateRefreshTokenUseCase generateRefreshTokenUseCase;

    private final BCryptPasswordEncoder bcrypt;

    public AuthenticationData execute(String email, String password) {
        log.info("Signing in user with email [{}]", email);

        try {
            Optional<User> userOptional = findUserBoundary.execute(email, null);

            if (userOptional.isEmpty()) {
                log.error("User with email [{}] not found", email);
                throw new BusinessViolationException(ErrorResponseCode.ECRITICAUTH_07);
            }

            User user = userOptional.get();

            if (!user.isActive()) {
                log.error("User with id [{}] is not active", user.getId());
                throw new BusinessViolationException(ErrorResponseCode.ECRITICAUTH_08);
            }

            boolean isPasswordValid = bcrypt.matches(password, user.getPassword());

            if (!isPasswordValid) {
                log.error("Invalid password for userId [{}]", user.getId());
                throw new BusinessViolationException(ErrorResponseCode.ECRITICAUTH_07);
            }

            AccessToken accessToken = generateAccessTokenUseCase.execute(user);
            RefreshToken refreshToken = generateRefreshTokenUseCase.execute(user);

            return AuthenticationData.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (DefaultException ex) {
            log.error("Error signing in user. Exception: [{}]", ex.getErrorResponse());
            throw ex;
        } catch (Exception ex) {
            log.error("Error signing in users", ex);
            throw ex;
        }
    }
}
