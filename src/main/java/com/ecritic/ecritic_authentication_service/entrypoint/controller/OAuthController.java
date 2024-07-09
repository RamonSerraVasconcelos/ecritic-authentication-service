package com.ecritic.ecritic_authentication_service.entrypoint.controller;

import com.ecritic.ecritic_authentication_service.core.usecase.oauth2.GenerateRedirectInfoUseCase;
import com.ecritic.ecritic_authentication_service.entrypoint.dto.AuthorizationUriResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<AuthorizationUriResponse> login(@RequestParam("authServerName") String authServerName, @RequestParam("redirectUri") String redirectUri) {
        URI authorizationUri = generateRedirectInfoUseCase.execute(authServerName, URI.create(redirectUri));

        AuthorizationUriResponse authorizationUriResponse = AuthorizationUriResponse.builder()
                .authorizationUri(authorizationUri.toString())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(authorizationUriResponse);
    }
}
