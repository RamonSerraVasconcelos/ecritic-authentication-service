package com.ecritic.ecritic_authentication_service.core.usecase.oauth2;

import com.ecritic.ecritic_authentication_service.core.fixture.AuthorizationServerFixture;
import com.ecritic.ecritic_authentication_service.core.model.AuthorizationServer;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2.CacheStateBoundary;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2.FindAuthServerByNameBoundary;
import com.ecritic.ecritic_authentication_service.exception.BusinessViolationException;
import com.ecritic.ecritic_authentication_service.exception.EntityNotFoundException;
import com.ecritic.ecritic_authentication_service.exception.InternalErrorException;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenerateRedirectInfoUseCaseTest {

    @InjectMocks
    private GenerateRedirectInfoUseCase generateRedirectInfoUseCase;

    @Mock
    private FindAuthServerByNameBoundary findAuthServerByNameBoundary;

    @Mock
    private CacheStateBoundary cacheStateBoundary;

    @Test
    void givenValidParams_whenCachingStateAndNecessaryData_thenGenerate_andReturnUri() {
        AuthorizationServer authorizationServer = AuthorizationServerFixture.load();

        when(findAuthServerByNameBoundary.execute(authorizationServer.getName())).thenReturn(Optional.of(authorizationServer));

        URI uri = generateRedirectInfoUseCase.execute(authorizationServer.getName(), authorizationServer.getRedirectUris().iterator().next());

        verify(findAuthServerByNameBoundary).execute(authorizationServer.getName());
        verify(cacheStateBoundary).execute(any(), any());

        assertThat(uri.getQuery())
                .contains("client_id=asikujdn9872hd892du2")
                .contains("response_type=code")
                .contains("redirect_uri=http://localhost:8080/oauth2/callback")
                .contains("scope=openid+profile+email+address+phone")
                .contains("state=")
                .contains("access_type=offline");
    }

    @Test
    void givenExecution_whenAuthorizationServerIsNotFound_thenThrowEntityNotFoundException() {
        String authServerName = "unexistent";
        when(findAuthServerByNameBoundary.execute(authServerName)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> generateRedirectInfoUseCase.execute(authServerName, URI.create("http://localhost:8080/oauth2/callback")));

        verify(findAuthServerByNameBoundary).execute(authServerName);
        verifyNoInteractions(cacheStateBoundary);

        assertThat(ex.getErrorResponse().getCode()).isEqualTo(ErrorResponseCode.ECRITICAUTH_09.getCode());
    }

    @Test
    void givenNotAllowedRedirectUri_thenThrowBusinessViolationException() {
        AuthorizationServer authorizationServer = AuthorizationServerFixture.load();

        when(findAuthServerByNameBoundary.execute(authorizationServer.getName())).thenReturn(Optional.of(authorizationServer));

        BusinessViolationException ex = assertThrows(BusinessViolationException.class,
                () -> generateRedirectInfoUseCase.execute(authorizationServer.getName(), URI.create("http://localhost:8080/callback")));

        verify(findAuthServerByNameBoundary).execute(authorizationServer.getName());
        verifyNoInteractions(cacheStateBoundary);

        assertThat(ex.getErrorResponse().getCode()).isEqualTo(ErrorResponseCode.ECRITICAUTH_10.getCode());
    }

    @Test
    void givenExecution_whenExceptionOccurs_thenThrowInternalErrorException() {
        doThrow(RuntimeException.class).when(findAuthServerByNameBoundary).execute(anyString());

        InternalErrorException ex = assertThrows(InternalErrorException.class,
                () -> generateRedirectInfoUseCase.execute("google", URI.create("http://localhost:8080/callback")));

        verify(findAuthServerByNameBoundary).execute(anyString());
        verifyNoInteractions(cacheStateBoundary);

        assertThat(ex.getErrorResponse().getCode()).isEqualTo(ErrorResponseCode.ECRITICAUTH_11.getCode());
    }
}