package com.ecritic.ecritic_authentication_service.entrypoint.controller;

import com.ecritic.ecritic_authentication_service.core.model.AuthenticationData;
import com.ecritic.ecritic_authentication_service.core.usecase.oauth2.GenerateRedirectInfoUseCase;
import com.ecritic.ecritic_authentication_service.core.usecase.oauth2.ValidateCallbackUseCase;
import com.ecritic.ecritic_authentication_service.entrypoint.dto.AuthenticationResponseData;
import com.ecritic.ecritic_authentication_service.entrypoint.dto.AuthorizationUriResponse;
import com.ecritic.ecritic_authentication_service.entrypoint.mapper.AuthenticationResponseDataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
public class OAuthController {

    private final GenerateRedirectInfoUseCase generateRedirectInfoUseCase;

    private final ValidateCallbackUseCase validateCallbackUseCase;

    private final AuthenticationResponseDataMapper authenticationResponseDataMapper;

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<AuthorizationUriResponse> login(@RequestParam("authServerName") String authServerName, @RequestParam("redirectUri") String redirectUri) {
        URI authorizationUri = generateRedirectInfoUseCase.execute(authServerName, URI.create(redirectUri));

        AuthorizationUriResponse authorizationUriResponse = AuthorizationUriResponse.builder()
                .authorizationUri(authorizationUri.toString())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(authorizationUriResponse);
    }

    @GetMapping(path = "/callback")
    public ResponseEntity<AuthenticationResponseData> callback(@RequestParam(value = "code", required = false) String code,
                                                               @RequestParam(value = "state", required = false) String state,
                                                               @RequestParam(value = "error", required = false) String error,
                                                               @RequestParam(value = "error_description", required = false) String errorDescription) {

        AuthenticationData authenticationData = validateCallbackUseCase.execute(code, state, error, errorDescription);

        AuthenticationResponseData responseData = authenticationResponseDataMapper.authorizationDataToAuthorizationResponseData(authenticationData);

        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }
}
