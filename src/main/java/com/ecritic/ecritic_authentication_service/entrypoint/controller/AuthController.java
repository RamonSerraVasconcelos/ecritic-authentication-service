package com.ecritic.ecritic_authentication_service.entrypoint.controller;

import com.ecritic.ecritic_authentication_service.core.model.AuthorizationData;
import com.ecritic.ecritic_authentication_service.core.usecase.SignInUserUseCase;
import com.ecritic.ecritic_authentication_service.entrypoint.dto.AuthorizationResponseData;
import com.ecritic.ecritic_authentication_service.entrypoint.mapper.AuthorizationResponseDataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SignInUserUseCase signInUserUseCase;

    private final AuthorizationResponseDataMapper authorizationResponseDataMapper;

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<AuthorizationResponseData> login(@RequestParam("email") String email,
                                                           @RequestParam("password") String password) {

        AuthorizationData authorizationData = signInUserUseCase.execute(email, password);

        AuthorizationResponseData responseData = authorizationResponseDataMapper.authorizationDataToAuthorizationResponseData(authorizationData);

        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }
}
