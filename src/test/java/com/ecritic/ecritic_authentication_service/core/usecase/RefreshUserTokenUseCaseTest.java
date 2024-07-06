package com.ecritic.ecritic_authentication_service.core.usecase;

import com.ecritic.ecritic_authentication_service.core.fixture.AccessTokenFixture;
import com.ecritic.ecritic_authentication_service.core.fixture.RefreshTokenFixture;
import com.ecritic.ecritic_authentication_service.core.fixture.UserFixture;
import com.ecritic.ecritic_authentication_service.core.model.AccessToken;
import com.ecritic.ecritic_authentication_service.core.model.AuthorizationData;
import com.ecritic.ecritic_authentication_service.core.model.RefreshToken;
import com.ecritic.ecritic_authentication_service.core.model.User;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.FindUserBoundary;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.SaveRefreshTokenBoundary;
import com.ecritic.ecritic_authentication_service.dataprovider.jwt.fixture.JwtFixture;
import com.ecritic.ecritic_authentication_service.exception.UnauthorizedAccessException;
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
class RefreshUserTokenUseCaseTest {

    @InjectMocks
    private RefreshUserTokenUseCase refreshUserTokenUseCase;

    @Mock
    private ValidateRefreshTokenUseCase validateRefreshTokenUseCase;

    @Mock
    private FindUserBoundary findUserBoundary;

    @Mock
    private GenerateAccessTokenUseCase generateAccessTokenUseCase;

    @Mock
    private GenerateRefreshTokenUseCase generateRefreshTokenUseCase;

    @Mock
    private SaveRefreshTokenBoundary saveRefreshTokenBoundary;

    @Test
    void givenJwtRefreshToken_whenTokenIsValid_thenGenerateAndReturnTokens() {
        User user = UserFixture.load();
        AccessToken accessToken = AccessTokenFixture.load();
        RefreshToken refreshToken = RefreshTokenFixture.load();
        String jwtToken = JwtFixture.loadSignedJwt();

        when(validateRefreshTokenUseCase.execute(jwtToken)).thenReturn(refreshToken);
        when(findUserBoundary.execute(null, refreshToken.getUser().getId())).thenReturn(Optional.of(user));
        when(generateAccessTokenUseCase.execute(user)).thenReturn(accessToken);
        when(generateRefreshTokenUseCase.execute(user)).thenReturn(refreshToken);
        when(saveRefreshTokenBoundary.execute(refreshToken)).thenReturn(refreshToken);

        AuthorizationData authorizationData = refreshUserTokenUseCase.execute(jwtToken);

        verify(validateRefreshTokenUseCase).execute(jwtToken);
        verify(findUserBoundary).execute(null, refreshToken.getUser().getId());
        verify(generateAccessTokenUseCase).execute(user);
        verify(generateRefreshTokenUseCase).execute(user);
        verify(saveRefreshTokenBoundary).execute(refreshToken);

        assertThat(authorizationData.getAccessToken()).isEqualTo(accessToken);
        assertThat(authorizationData.getRefreshToken()).isEqualTo(refreshToken);
    }

    @Test
    void givenExecution_thenUserIsNotFound_thenThrowUnauthorizedAccessException() {
        RefreshToken refreshToken = RefreshTokenFixture.load();
        String jwtToken = JwtFixture.loadSignedJwt();

        when(validateRefreshTokenUseCase.execute(jwtToken)).thenReturn(refreshToken);
        when(findUserBoundary.execute(null, refreshToken.getUser().getId())).thenReturn(Optional.empty());

        UnauthorizedAccessException ex = assertThrows(UnauthorizedAccessException.class, () -> refreshUserTokenUseCase.execute(jwtToken));

        verify(validateRefreshTokenUseCase).execute(jwtToken);
        verify(findUserBoundary).execute(null, refreshToken.getUser().getId());
        verifyNoInteractions(generateAccessTokenUseCase);
        verifyNoInteractions(generateRefreshTokenUseCase);
        verifyNoInteractions(saveRefreshTokenBoundary);

        assertThat(ex.getErrorResponse().getCode()).isEqualTo(ErrorResponseCode.ECRITICAUTH_03.getCode());
    }

    @Test
    void givenExecution_whenUserIsNotActive_thenThrowUnauthorizedAccessException() {
        RefreshToken refreshToken = RefreshTokenFixture.load();
        String jwtToken = JwtFixture.loadSignedJwt();
        User user = UserFixture.load();
        user.setActive(false);

        when(validateRefreshTokenUseCase.execute(jwtToken)).thenReturn(refreshToken);
        when(findUserBoundary.execute(null, refreshToken.getUser().getId())).thenReturn(Optional.of(user));

        UnauthorizedAccessException ex = assertThrows(UnauthorizedAccessException.class, () -> refreshUserTokenUseCase.execute(jwtToken));

        verify(validateRefreshTokenUseCase).execute(jwtToken);
        verify(findUserBoundary).execute(null, refreshToken.getUser().getId());
        verifyNoInteractions(generateAccessTokenUseCase);
        verifyNoInteractions(generateRefreshTokenUseCase);
        verifyNoInteractions(saveRefreshTokenBoundary);

        assertThat(ex.getErrorResponse().getCode()).isEqualTo(ErrorResponseCode.ECRITICAUTH_03.getCode());
    }
}