package com.ecritic.ecritic_authentication_service.dataprovider.jwt.impl;

import com.ecritic.ecritic_authentication_service.core.model.Token;
import com.ecritic.ecritic_authentication_service.core.model.enums.Role;
import com.ecritic.ecritic_authentication_service.dataprovider.jwt.fixture.JwtFixture;
import com.ecritic.ecritic_authentication_service.exception.UnauthorizedAccessException;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.SecretKey;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ValidateJwtTokenGatewayTest {

    @InjectMocks
    private ValidateJwtTokenGateway validateJwtTokenGateway;

    @Test
    void givenValidJwtToken_thenValidate_andReturnToken() {
        String jwt = JwtFixture.loadSignedJwt();
        SecretKey secretKey = JwtFixture.loadSecret();

        Token token = validateJwtTokenGateway.execute(jwt, secretKey);

        assertThat(token).isNotNull();
        assertThat(token.getIssuer()).isEqualTo("ecritic");
        assertThat(token.getAud()).isEqualTo(Set.of("ecritic"));
        assertThat(token.getUser().getId()).isNotNull();
        assertThat(token.getUser().getRole()).isEqualTo(Role.DEFAULT);
        assertThat(token.getIssuedAt()).isNotNull();
        assertThat(token.getExpiresAt()).isNotNull();
    }

    @Test
    void givenInvalidJwtToken_whenSignatureIsInvalid_thenThrowUnauthorizedAccessException() {
        String jwt = JwtFixture.loadSignedWithDifferentKey();
        SecretKey secretKey = JwtFixture.loadSecret();

        UnauthorizedAccessException ex = assertThrows(UnauthorizedAccessException.class,
                () -> validateJwtTokenGateway.execute(jwt, secretKey));

        assertThat(ex.getErrorResponse().getCode()).isEqualTo(ErrorResponseCode.ECRITICAUTH_03.getCode());
    }

    @Test
    void givenInvalidJwtToken_whenTokenIsMalformed_thenThrowUnauthorizedAccessException() {
        String malFormedToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhd";
        SecretKey secretKey = JwtFixture.loadSecret();

        UnauthorizedAccessException ex = assertThrows(UnauthorizedAccessException.class, () -> validateJwtTokenGateway.execute(malFormedToken, secretKey));

        assertThat(ex.getErrorResponse().getCode()).isEqualTo(ErrorResponseCode.ECRITICAUTH_03.getCode());
    }

    @Test
    void givenInvalidJwtToken_whenTokenIsExpired_thenThrowUnauthorizedAccessException() {
        String expiredJwtToken = JwtFixture.loadExpiredJwt();
        SecretKey secretKey = JwtFixture.loadSecret();

        UnauthorizedAccessException ex = assertThrows(UnauthorizedAccessException.class, () -> validateJwtTokenGateway.execute(expiredJwtToken, secretKey));
    }
}