package com.ecritic.ecritic_authentication_service.entrypoint.controller;

import com.ecritic.ecritic_authentication_service.core.model.AccessToken;
import com.ecritic.ecritic_authentication_service.core.model.AuthorizationData;
import com.ecritic.ecritic_authentication_service.core.usecase.DecryptAccessTokenUseCase;
import com.ecritic.ecritic_authentication_service.core.usecase.RefreshUserTokenUseCase;
import com.ecritic.ecritic_authentication_service.core.usecase.SignInUserUseCase;
import com.ecritic.ecritic_authentication_service.entrypoint.dto.AuthorizationResponseData;
import com.ecritic.ecritic_authentication_service.entrypoint.dto.RefreshTokenRequest;
import com.ecritic.ecritic_authentication_service.entrypoint.mapper.AuthorizationResponseDataMapper;
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

    private final AuthorizationResponseDataMapper authorizationResponseDataMapper;

    private final Validator validator;

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<AuthorizationResponseData> login(@RequestParam("email") String email,
                                                           @RequestParam("password") String password) {

        AuthorizationData authorizationData = signInUserUseCase.execute(email, password);

        AuthorizationResponseData responseData = authorizationResponseDataMapper.authorizationDataToAuthorizationResponseData(authorizationData);

        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    @PostMapping(path = "/token")
    public ResponseEntity<AuthorizationResponseData> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        Set<ConstraintViolation<RefreshTokenRequest>> violations = validator.validate(refreshTokenRequest);
        if (!violations.isEmpty()) {
            throw new ResourceViolationException(violations);
        }

        AuthorizationData authorizationData = refreshUserTokenUseCase.execute(refreshTokenRequest.getRefreshToken());

        AuthorizationResponseData responseData = authorizationResponseDataMapper.authorizationDataToAuthorizationResponseData(authorizationData);

        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    @PostMapping("/token/introspect")
    public ResponseEntity<AuthorizationResponseData> introspect(@RequestHeader("Authorization") String jwToken) {
        if (isNull(jwToken)) {
            throw new UnauthorizedAccessException(ErrorResponseCode.ECRITICAUTH_03);
        }

        AccessToken accessToken = decryptAccessTokenUseCase.execute(jwToken);

        AuthorizationResponseData authorizationResponseData = AuthorizationResponseData.builder()
                .accessToken(accessToken.getJwt())
                .tokenType("Bearer")
                .expiresIn((int) accessToken.getExpiresAt().toEpochSecond(ZoneOffset.UTC))
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(authorizationResponseData);
    }
}
