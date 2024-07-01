package com.ecritic.ecritic_authentication_service.entrypoint.controller;

import com.ecritic.ecritic_authentication_service.core.model.AccessToken;
import com.ecritic.ecritic_authentication_service.core.model.AuthorizationData;
import com.ecritic.ecritic_authentication_service.core.usecase.DecryptAccessTokenUseCase;
import com.ecritic.ecritic_authentication_service.core.usecase.SignInUserUseCase;
import com.ecritic.ecritic_authentication_service.entrypoint.dto.AuthorizationResponseData;
import com.ecritic.ecritic_authentication_service.entrypoint.mapper.AuthorizationResponseDataMapper;
import com.ecritic.ecritic_authentication_service.exception.UnauthorizedAccessException;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneOffset;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SignInUserUseCase signInUserUseCase;

    private final DecryptAccessTokenUseCase decryptAccessTokenUseCase;

    private final AuthorizationResponseDataMapper authorizationResponseDataMapper;

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<AuthorizationResponseData> login(@RequestParam("email") String email,
                                                           @RequestParam("password") String password) {

        AuthorizationData authorizationData = signInUserUseCase.execute(email, password);

        AuthorizationResponseData responseData = authorizationResponseDataMapper.authorizationDataToAuthorizationResponseData(authorizationData);

        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    @PostMapping("/introspect")
    public ResponseEntity<AuthorizationResponseData> introspect(@RequestHeader("Authorization") String jwToken) {
        AccessToken accessToken = decryptAccessTokenUseCase.execute(extractToken(jwToken));

        AuthorizationResponseData authorizationResponseData = AuthorizationResponseData.builder()
                .accessToken(accessToken.getJwt())
                .tokenType("Bearer")
                .expiresIn((int) accessToken.getExpiresAt().toEpochSecond(ZoneOffset.UTC))
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(authorizationResponseData);
    }

    private String extractToken(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        throw new UnauthorizedAccessException(ErrorResponseCode.ECRITICAUTH_03);
    }
}
