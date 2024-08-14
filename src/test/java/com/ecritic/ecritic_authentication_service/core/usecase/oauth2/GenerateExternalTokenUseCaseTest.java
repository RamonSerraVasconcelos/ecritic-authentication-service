package com.ecritic.ecritic_authentication_service.core.usecase.oauth2;

import com.ecritic.ecritic_authentication_service.core.fixture.AuthorizationRequestFixture;
import com.ecritic.ecritic_authentication_service.core.fixture.AuthorizationServerFixture;
import com.ecritic.ecritic_authentication_service.core.fixture.ExternalTokenFixture;
import com.ecritic.ecritic_authentication_service.core.model.AuthorizationRequest;
import com.ecritic.ecritic_authentication_service.core.model.AuthorizationServer;
import com.ecritic.ecritic_authentication_service.core.model.ExternalToken;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2.GenerateExternalTokenBoundary;
import com.ecritic.ecritic_authentication_service.exception.BusinessViolationException;
import com.ecritic.ecritic_authentication_service.exception.InternalErrorException;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenerateExternalTokenUseCaseTest {

    @InjectMocks
    private GenerateExternalTokenUseCase generateExternalTokenUseCase;

    @Mock
    private GenerateExternalTokenBoundary generateExternalTokenBoundary;

    @Test
    void givenExecution_thenGenerate_andReturnExternalToken() {
        AuthorizationRequest authorizationRequest = AuthorizationRequestFixture.load();
        AuthorizationServer authorizationServer = AuthorizationServerFixture.load();
        ExternalToken externalToken = ExternalTokenFixture.load();
        String code = "asodij82";

        when(generateExternalTokenBoundary.execute(authorizationRequest, authorizationServer, code)).thenReturn(externalToken);

        ExternalToken result = generateExternalTokenUseCase.execute(authorizationRequest, authorizationServer, code);

        verify(generateExternalTokenBoundary).execute(authorizationRequest, authorizationServer, code);

        assertThat(result).usingRecursiveComparison().isEqualTo(externalToken);
    }

    @Test
    void givenDefaultException_thenRethrowException() {
        doThrow(new BusinessViolationException(ErrorResponseCode.ECRITICAUTH_13)).when(generateExternalTokenBoundary).execute(any(), any(), any());

        BusinessViolationException ex = assertThrows(BusinessViolationException.class, () -> generateExternalTokenUseCase.execute(AuthorizationRequestFixture.load(), AuthorizationServerFixture.load(), "asodij82"));

        verify(generateExternalTokenBoundary).execute(any(), any(), any());
        assertThat(ex.getErrorResponse().getCode()).isEqualTo(ErrorResponseCode.ECRITICAUTH_13.getCode());
    }

    @Test
    void givenException_thenThrowInternalErrorException() {
        doThrow(new RuntimeException()).when(generateExternalTokenBoundary).execute(any(), any(), any());

        InternalErrorException ex = assertThrows(InternalErrorException.class, () ->
                generateExternalTokenUseCase.execute(AuthorizationRequestFixture.load(), AuthorizationServerFixture.load(), "asodij82"));

        verify(generateExternalTokenBoundary).execute(any(), any(), any());
        assertThat(ex.getErrorResponse().getCode()).isEqualTo(ErrorResponseCode.ECRITICAUTH_11.getCode());
    }
}