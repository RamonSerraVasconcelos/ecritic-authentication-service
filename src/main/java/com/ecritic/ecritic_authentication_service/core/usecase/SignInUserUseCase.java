package com.ecritic.ecritic_authentication_service.core.usecase;

import com.ecritic.ecritic_authentication_service.core.model.AccessToken;
import com.ecritic.ecritic_authentication_service.core.model.AuthorizationData;
import com.ecritic.ecritic_authentication_service.core.model.RefreshToken;
import com.ecritic.ecritic_authentication_service.core.model.User;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.FindUserByEmailBoundary;
import com.ecritic.ecritic_authentication_service.exception.BusinessViolationException;
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

    private final FindUserByEmailBoundary findUserByEmailBoundary;

    private final GenerateAccessTokenUseCase generateAccessTokenUseCase;

    private final BCryptPasswordEncoder bcrypt;

    public AuthorizationData execute(String email, String password) {
        log.info("Signing in user with email [{}]", email);

        Optional<User> userOptional = findUserByEmailBoundary.execute(email);

        if (userOptional.isEmpty()) {
            throw new BusinessViolationException(ErrorResponseCode.ECRITICAUTH_07);
        }

        User user = userOptional.get();

        if (!user.isActive()) {
            throw new BusinessViolationException(ErrorResponseCode.ECRITICAUTH_08);
        }

        boolean isPasswordValid = bcrypt.matches(password, user.getPassword());

        if (!isPasswordValid) {
            throw new BusinessViolationException(ErrorResponseCode.ECRITICAUTH_07);
        }

        AccessToken accessToken = generateAccessTokenUseCase.execute(user);

        return AuthorizationData.builder()
                .accessToken(accessToken)
                .build();
    }
}
