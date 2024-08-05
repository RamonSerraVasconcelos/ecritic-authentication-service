package com.ecritic.ecritic_authentication_service.core.usecase.oauth2;

import com.ecritic.ecritic_authentication_service.core.fixture.AuthorizationServerFixture;
import com.ecritic.ecritic_authentication_service.core.fixture.IdTokenFixture;
import com.ecritic.ecritic_authentication_service.core.model.AuthorizationServer;
import com.ecritic.ecritic_authentication_service.core.model.IdToken;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2.FindAuthServerJwksBoundary;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2.ValidateIdTokenBoundary;
import com.ecritic.ecritic_authentication_service.exception.BusinessViolationException;
import com.ecritic.ecritic_authentication_service.exception.InternalErrorException;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;
import com.nimbusds.jose.jwk.JWKSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateIdTokenUseCaseTest {

    @InjectMocks
    private ValidateIdTokenUseCase validateIdTokenUseCase;

    @Mock
    private FindAuthServerJwksBoundary findAuthServerJwksBoundary;

    @Mock
    private ValidateIdTokenBoundary validateIdTokenBoundary;

    @Test
    void givenValidParams_whenJwksIsFound_thenValidate_andReturnIdToken() {
        JWKSet jwkSet = new JWKSet();
        AuthorizationServer authorizationServer = AuthorizationServerFixture.load();
        IdToken idTokenFixture = IdTokenFixture.load();
        idTokenFixture.setAud(Set.of(authorizationServer.getClientId()));

        when(findAuthServerJwksBoundary.execute(any())).thenReturn(jwkSet);
        when(validateIdTokenBoundary.execute(idTokenFixture.getJwt(), jwkSet)).thenReturn(idTokenFixture);

        IdToken idToken = validateIdTokenUseCase.execute(idTokenFixture.getJwt(), authorizationServer);

        verify(findAuthServerJwksBoundary).execute(authorizationServer.getJwksEndpoint());
        verify(validateIdTokenBoundary).execute(idTokenFixture.getJwt(), jwkSet);

        assertThat(idToken).isNotNull().usingRecursiveComparison().isEqualTo(idTokenFixture);
    }

    @Test
    void givenExecution_whenIdTokenIsExpired_thenThrowBusinessViolationException() {
        JWKSet jwkSet = new JWKSet();
        AuthorizationServer authorizationServer = AuthorizationServerFixture.load();
        IdToken idTokenFixture = IdTokenFixture.load();
        idTokenFixture.setExpiresAt(LocalDateTime.now().minusMinutes(6));

        when(findAuthServerJwksBoundary.execute(any())).thenReturn(jwkSet);
        when(validateIdTokenBoundary.execute(idTokenFixture.getJwt(), jwkSet)).thenReturn(idTokenFixture);

        BusinessViolationException ex = assertThrows(BusinessViolationException.class, () -> {
            validateIdTokenUseCase.execute(idTokenFixture.getJwt(), authorizationServer);
        });

        verify(findAuthServerJwksBoundary).execute(authorizationServer.getJwksEndpoint());
        verify(validateIdTokenBoundary).execute(idTokenFixture.getJwt(), jwkSet);

        assertThat(ex.getErrorResponse().getCode()).isEqualTo(ErrorResponseCode.ECRITICAUTH_12.getCode());
    }

    @Test
    void givenExecution_whenIdTokenAudienceDoesNotMatchClientId_thenThrowBusinessViolationException() {
        JWKSet jwkSet = new JWKSet();
        AuthorizationServer authorizationServer = AuthorizationServerFixture.load();
        IdToken idTokenFixture = IdTokenFixture.load();
        idTokenFixture.setAud(Set.of("invalidAudience"));

        when(findAuthServerJwksBoundary.execute(any())).thenReturn(jwkSet);
        when(validateIdTokenBoundary.execute(idTokenFixture.getJwt(), jwkSet)).thenReturn(idTokenFixture);

        BusinessViolationException ex = assertThrows(BusinessViolationException.class, () -> {
            validateIdTokenUseCase.execute(idTokenFixture.getJwt(), authorizationServer);
        });

        verify(findAuthServerJwksBoundary).execute(authorizationServer.getJwksEndpoint());
        verify(validateIdTokenBoundary).execute(idTokenFixture.getJwt(), jwkSet);

        assertThat(ex.getErrorResponse().getCode()).isEqualTo(ErrorResponseCode.ECRITICAUTH_12.getCode());
    }

    @Test
    void givenExecution_whenExceptionOccurs_thenThrowInternalErrorException() {
        AuthorizationServer authorizationServer = AuthorizationServerFixture.load();

        InternalErrorException ex = assertThrows(InternalErrorException.class, () -> {
            when(findAuthServerJwksBoundary.execute(any())).thenThrow(new RuntimeException());
            validateIdTokenUseCase.execute("eynasdiasoidasjiod", authorizationServer);
        });

        verify(findAuthServerJwksBoundary).execute(any());
        verifyNoInteractions(validateIdTokenBoundary);

        assertThat(ex.getErrorResponse().getCode()).isEqualTo(ErrorResponseCode.ECRITICAUTH_11.getCode());
    }
}