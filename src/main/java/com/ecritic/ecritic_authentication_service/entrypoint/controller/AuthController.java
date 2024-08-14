package com.ecritic.ecritic_authentication_service.entrypoint.controller;

import com.ecritic.ecritic_authentication_service.core.model.AccessToken;
import com.ecritic.ecritic_authentication_service.core.model.AuthenticationData;
import com.ecritic.ecritic_authentication_service.core.usecase.DecryptAccessTokenUseCase;
import com.ecritic.ecritic_authentication_service.core.usecase.RefreshUserTokenUseCase;
import com.ecritic.ecritic_authentication_service.core.usecase.SignInUserUseCase;
import com.ecritic.ecritic_authentication_service.entrypoint.dto.AuthenticationResponseData;
import com.ecritic.ecritic_authentication_service.entrypoint.dto.RefreshTokenRequest;
import com.ecritic.ecritic_authentication_service.entrypoint.mapper.AuthenticationResponseDataMapper;
import com.ecritic.ecritic_authentication_service.exception.ResourceViolationException;
import com.ecritic.ecritic_authentication_service.exception.UnauthorizedAccessException;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneOffset;
import java.util.Set;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SignInUserUseCase signInUserUseCase;

    private final DecryptAccessTokenUseCase decryptAccessTokenUseCase;

    private final RefreshUserTokenUseCase refreshUserTokenUseCase;

    private final AuthenticationResponseDataMapper authenticationResponseDataMapper;

    private final Validator validator;

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<AuthenticationResponseData> login(@RequestParam("email") String email,
                                                            @RequestParam("password") String password) {

        AuthenticationData authenticationData = signInUserUseCase.execute(email, password);

        AuthenticationResponseData responseData = authenticationResponseDataMapper.authorizationDataToAuthorizationResponseData(authenticationData);

        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    @PostMapping(path = "/token")
    public ResponseEntity<AuthenticationResponseData> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        Set<ConstraintViolation<RefreshTokenRequest>> violations = validator.validate(refreshTokenRequest);
        if (!violations.isEmpty()) {
            throw new ResourceViolationException(violations);
        }

        AuthenticationData authenticationData = refreshUserTokenUseCase.execute(refreshTokenRequest.getRefreshToken());

        AuthenticationResponseData responseData = authenticationResponseDataMapper.authorizationDataToAuthorizationResponseData(authenticationData);

        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    @PostMapping("/token/introspect")
    public ResponseEntity<AuthenticationResponseData> introspect(@RequestHeader("Authorization") String jwToken) {
        if (isNull(jwToken)) {
            throw new UnauthorizedAccessException(ErrorResponseCode.ECRITICAUTH_03);
        }

        AccessToken accessToken = decryptAccessTokenUseCase.execute(jwToken);

        AuthenticationResponseData authenticationResponseData = AuthenticationResponseData.builder()
                .accessToken(accessToken.getJwt())
                .tokenType("Bearer")
                .expiresIn((int) accessToken.getExpiresAt().toEpochSecond(ZoneOffset.UTC))
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(authenticationResponseData);
    }
}
