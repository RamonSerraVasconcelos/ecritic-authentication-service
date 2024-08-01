package com.ecritic.ecritic_authentication_service.core.usecase.oauth2;

import com.ecritic.ecritic_authentication_service.core.fixture.AccessTokenFixture;
import com.ecritic.ecritic_authentication_service.core.fixture.AuthorizationRequestFixture;
import com.ecritic.ecritic_authentication_service.core.fixture.AuthorizationServerFixture;
import com.ecritic.ecritic_authentication_service.core.fixture.ExternalTokenFixture;
import com.ecritic.ecritic_authentication_service.core.fixture.IdTokenFixture;
import com.ecritic.ecritic_authentication_service.core.fixture.RefreshTokenFixture;
import com.ecritic.ecritic_authentication_service.core.fixture.UserFixture;
import com.ecritic.ecritic_authentication_service.core.model.AccessToken;
import com.ecritic.ecritic_authentication_service.core.model.AuthenticationData;
import com.ecritic.ecritic_authentication_service.core.model.AuthorizationRequest;
import com.ecritic.ecritic_authentication_service.core.model.AuthorizationServer;
import com.ecritic.ecritic_authentication_service.core.model.ExternalToken;
import com.ecritic.ecritic_authentication_service.core.model.IdToken;
import com.ecritic.ecritic_authentication_service.core.model.RefreshToken;
import com.ecritic.ecritic_authentication_service.core.model.User;
import com.ecritic.ecritic_authentication_service.core.usecase.GenerateAccessTokenUseCase;
import com.ecritic.ecritic_authentication_service.core.usecase.GenerateRefreshTokenUseCase;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2.DeleteStateBoundary;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2.DeleteUserExternalTokensBoundary;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2.FindAuthServerByClientIdBoundary;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2.FindStateBoundary;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2.SaveExternalTokenBoundary;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2.UpsertUserBoundary;
import com.ecritic.ecritic_authentication_service.exception.BusinessViolationException;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateCallbackUseCaseTest {

    @InjectMocks
    private ValidateCallbackUseCase validateCallbackUseCase;

    @Mock
    private FindStateBoundary findCachedStateBoundary;

    @Mock
    private FindAuthServerByClientIdBoundary findAuthServerByClientIdBoundary;

    @Mock
    private GenerateExternalTokenUseCase generateExternalTokenUseCase;

    @Mock
    private ValidateIdTokenUseCase validateIdTokenUseCase;

    @Mock
    private UpsertUserBoundary upsertUserBoundary;

    @Mock
    private SaveExternalTokenBoundary saveExternalTokenBoundary;

    @Mock
    private DeleteStateBoundary deleteStateBoundary;

    @Mock
    private GenerateAccessTokenUseCase generateAccessTokenUseCase;

    @Mock
    private GenerateRefreshTokenUseCase generateRefreshTokenUseCase;

    @Mock
    private DeleteUserExternalTokensBoundary deleteUserExternalTokensBoundary;

    private static final String STATE = "udn9182d";
    private static final String CODE = "dsau9idh829dhj9821d9sd";

    @Test
    void givenValidCallbackParams_whenAuthorizationCodeExchangeIsSuccessfull_thenReturnAuthenticationData() {
        AuthorizationRequest authorizationRequest = AuthorizationRequestFixture.load();
        AuthorizationServer authorizationServer = AuthorizationServerFixture.load();
        ExternalToken externalToken = ExternalTokenFixture.load();
        IdToken idToken = IdTokenFixture.load();
        User user = UserFixture.load();
        AccessToken accessToken = AccessTokenFixture.load();
        RefreshToken refreshToken = RefreshTokenFixture.load();

        when(findCachedStateBoundary.execute(STATE)).thenReturn(Optional.of(authorizationRequest));
        when(findAuthServerByClientIdBoundary.execute(authorizationRequest.getClientId())).thenReturn(Optional.of(authorizationServer));
        when(generateExternalTokenUseCase.execute(authorizationRequest, authorizationServer, CODE)).thenReturn(externalToken);
        when(validateIdTokenUseCase.execute(externalToken.getIdToken(), authorizationServer)).thenReturn(idToken);
        when(upsertUserBoundary.execute(idToken.getEmail(), idToken.getName())).thenReturn(user);
        when(saveExternalTokenBoundary.execute(externalToken)).thenReturn(externalToken);
        when(generateAccessTokenUseCase.execute(user)).thenReturn(accessToken);
        when(generateRefreshTokenUseCase.execute(user)).thenReturn(refreshToken);

        AuthenticationData authenticationData = validateCallbackUseCase.execute(CODE, STATE, null, null);

        verify(findCachedStateBoundary).execute(STATE);
        verify(findAuthServerByClientIdBoundary).execute(authorizationRequest.getClientId());
        verify(generateExternalTokenUseCase).execute(authorizationRequest, authorizationServer, CODE);
        verify(validateIdTokenUseCase).execute(externalToken.getIdToken(), authorizationServer);
        verify(upsertUserBoundary).execute(idToken.getEmail(), idToken.getName());
        verify(saveExternalTokenBoundary).execute(externalToken);
        verify(generateAccessTokenUseCase).execute(user);
        verify(generateRefreshTokenUseCase).execute(user);

        assertThat(authenticationData).isNotNull();
        assertThat(authenticationData.getAccessToken()).usingRecursiveComparison().isEqualTo(accessToken);
        assertThat(authenticationData.getRefreshToken()).usingRecursiveComparison().isEqualTo(refreshToken);
    }

    @Test
    void givenPresentErrorParams_thenThrowBusinessViolationException() {
        String error = "invalid_grant";
        String errorDescription = "Invalid Grant";

        BusinessViolationException ex = assertThrows(BusinessViolationException.class,
                () -> validateCallbackUseCase.execute(null, null, error, errorDescription));

        verifyNoInteractions(findCachedStateBoundary);
        verifyNoInteractions(findAuthServerByClientIdBoundary);
        verifyNoInteractions(generateExternalTokenUseCase);
        verifyNoInteractions(validateIdTokenUseCase);
        verifyNoInteractions(upsertUserBoundary);
        verifyNoInteractions(saveExternalTokenBoundary);
        verifyNoInteractions(generateAccessTokenUseCase);
        verifyNoInteractions(generateRefreshTokenUseCase);
        verifyNoInteractions(deleteStateBoundary);
        verifyNoInteractions(deleteUserExternalTokensBoundary);

        assertThat(ex.getErrorResponse().getCode()).isEqualTo(ErrorResponseCode.ECRITICAUTH_14.getCode());
    }

    @Test
    void givenInvalidState_whenAuthorizationRequestInfoIsNotFound_thenThrowBusinnessViolationException() {
        String state = "invalidState";

        when(findCachedStateBoundary.execute(state)).thenReturn(Optional.empty());

        BusinessViolationException ex = assertThrows(BusinessViolationException.class,
                () -> validateCallbackUseCase.execute(CODE, state, null, null));

        verify(findCachedStateBoundary).execute(state);
        verifyNoInteractions(findAuthServerByClientIdBoundary);
        verifyNoInteractions(generateExternalTokenUseCase);
        verifyNoInteractions(validateIdTokenUseCase);
        verifyNoInteractions(upsertUserBoundary);
        verifyNoInteractions(saveExternalTokenBoundary);
        verifyNoInteractions(generateAccessTokenUseCase);
        verifyNoInteractions(generateRefreshTokenUseCase);
        verifyNoInteractions(deleteStateBoundary);
        verifyNoInteractions(deleteUserExternalTokensBoundary);

        assertThat(ex.getErrorResponse().getCode()).isEqualTo(ErrorResponseCode.ECRITICAUTH_12.getCode());
    }

    @Test
    void givenValidState_whenAuthorizationServerIsNotFound_thenThrowBusinessViolationException() {
        AuthorizationRequest authorizationRequest = AuthorizationRequestFixture.load();

        when(findCachedStateBoundary.execute(STATE)).thenReturn(Optional.of(authorizationRequest));
        when(findAuthServerByClientIdBoundary.execute(authorizationRequest.getClientId())).thenReturn(Optional.empty());

        BusinessViolationException ex = assertThrows(BusinessViolationException.class,
                () -> validateCallbackUseCase.execute(CODE, STATE, null, null));

        verify(findCachedStateBoundary).execute(STATE);
        verify(findAuthServerByClientIdBoundary).execute(authorizationRequest.getClientId());
        verifyNoInteractions(generateExternalTokenUseCase);
        verifyNoInteractions(validateIdTokenUseCase);
        verifyNoInteractions(upsertUserBoundary);
        verifyNoInteractions(saveExternalTokenBoundary);
        verifyNoInteractions(generateAccessTokenUseCase);
        verifyNoInteractions(generateRefreshTokenUseCase);
        verifyNoInteractions(deleteStateBoundary);
        verifyNoInteractions(deleteUserExternalTokensBoundary);

        assertThat(ex.getErrorResponse().getCode()).isEqualTo(ErrorResponseCode.ECRITICAUTH_12.getCode());
    }

    @Test
    void givenValidParams_whenUserIsNotActive_thenThrowBusinessViolationException() {
        AuthorizationRequest authorizationRequest = AuthorizationRequestFixture.load();
        AuthorizationServer authorizationServer = AuthorizationServerFixture.load();
        ExternalToken externalToken = ExternalTokenFixture.load();
        IdToken idToken = IdTokenFixture.load();
        User user = UserFixture.load();
        user.setActive(false);

        when(findCachedStateBoundary.execute(STATE)).thenReturn(Optional.of(authorizationRequest));
        when(findAuthServerByClientIdBoundary.execute(authorizationRequest.getClientId())).thenReturn(Optional.of(authorizationServer));
        when(generateExternalTokenUseCase.execute(authorizationRequest, authorizationServer, CODE)).thenReturn(externalToken);
        when(validateIdTokenUseCase.execute(externalToken.getIdToken(), authorizationServer)).thenReturn(idToken);
        when(upsertUserBoundary.execute(idToken.getEmail(), idToken.getName())).thenReturn(user);

        BusinessViolationException ex = assertThrows(BusinessViolationException.class,
                () -> validateCallbackUseCase.execute(CODE, STATE, null, null));

        verify(findCachedStateBoundary).execute(STATE);
        verify(findAuthServerByClientIdBoundary).execute(authorizationRequest.getClientId());
        verify(generateExternalTokenUseCase).execute(authorizationRequest, authorizationServer, CODE);
        verify(validateIdTokenUseCase).execute(externalToken.getIdToken(), authorizationServer);
        verify(upsertUserBoundary).execute(idToken.getEmail(), idToken.getName());
        verifyNoInteractions(saveExternalTokenBoundary);
        verifyNoInteractions(generateAccessTokenUseCase);
        verifyNoInteractions(generateRefreshTokenUseCase);
        verifyNoInteractions(deleteStateBoundary);
        verifyNoInteractions(deleteUserExternalTokensBoundary);

        assertThat(ex.getErrorResponse().getCode()).isEqualTo(ErrorResponseCode.ECRITICAUTH_08.getCode());
    }
}