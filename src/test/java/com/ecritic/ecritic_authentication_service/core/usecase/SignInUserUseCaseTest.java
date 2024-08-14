package com.ecritic.ecritic_authentication_service.core.usecase;

import com.ecritic.ecritic_authentication_service.core.fixture.AccessTokenFixture;
import com.ecritic.ecritic_authentication_service.core.fixture.RefreshTokenFixture;
import com.ecritic.ecritic_authentication_service.core.fixture.UserFixture;
import com.ecritic.ecritic_authentication_service.core.model.AccessToken;
import com.ecritic.ecritic_authentication_service.core.model.AuthenticationData;
import com.ecritic.ecritic_authentication_service.core.model.RefreshToken;
import com.ecritic.ecritic_authentication_service.core.model.User;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.FindUserBoundary;
import com.ecritic.ecritic_authentication_service.exception.BusinessViolationException;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SignInUserUseCaseTest {

    @InjectMocks
    private SignInUserUseCase signInUserUseCase;

    @Mock
    private FindUserBoundary findUserBoundary;

    @Mock
    private GenerateAccessTokenUseCase generateAccessTokenUseCase;

    @Mock
    private GenerateRefreshTokenUseCase generateRefreshTokenUseCase;

    @Mock
    private BCryptPasswordEncoder bcrypt;

    @Test
    void givenValidAuthInfo_thenGenerateAndReturnUserTokens() {
        User user = UserFixture.load();
        AccessToken accessToken = AccessTokenFixture.load();
        RefreshToken refreshToken = RefreshTokenFixture.load();

        when(findUserBoundary.execute(user.getEmail(), null)).thenReturn(Optional.of(user));
        when(bcrypt.matches(any(), any())).thenReturn(true);
        when(generateAccessTokenUseCase.execute(user)).thenReturn(accessToken);
        when(generateRefreshTokenUseCase.execute(user)).thenReturn(refreshToken);

        AuthenticationData authenticationData = signInUserUseCase.execute(user.getEmail(), user.getPassword());

        verify(findUserBoundary).execute(user.getEmail(), null);
        verify(bcrypt).matches(any(), any());
        verify(generateAccessTokenUseCase).execute(user);
        verify(generateRefreshTokenUseCase).execute(user);

        assertThat(authenticationData).isNotNull();
        assertThat(authenticationData.getAccessToken()).usingRecursiveComparison().isEqualTo(accessToken);
        assertThat(authenticationData.getRefreshToken()).usingRecursiveComparison().isEqualTo(refreshToken);
    }

    @Test
    void givenExecution_whenUserIsNotFound_thenThrowBusinessViolationException() {
        when(findUserBoundary.execute(any(), any())).thenReturn(Optional.empty());

        BusinessViolationException ex = assertThrows(BusinessViolationException.class, () -> signInUserUseCase.execute("email", "password"));

        verify(findUserBoundary).execute(any(), any());
        verifyNoInteractions(bcrypt);
        verifyNoInteractions(generateAccessTokenUseCase);
        verifyNoInteractions(generateRefreshTokenUseCase);

        assertThat(ex.getErrorResponse().getCode()).isEqualTo(ErrorResponseCode.ECRITICAUTH_07.getCode());
    }

    @Test
    void givenInactiveUser_thenThrowBusinessViolationException() {
        User user = UserFixture.load();
        user.setActive(false);

        when(findUserBoundary.execute(any(), any())).thenReturn(Optional.of(user));

        BusinessViolationException ex = assertThrows(BusinessViolationException.class, () -> signInUserUseCase.execute("email", "password"));

        verify(findUserBoundary).execute(any(), any());
        verifyNoInteractions(bcrypt);
        verifyNoInteractions(generateAccessTokenUseCase);
        verifyNoInteractions(generateRefreshTokenUseCase);

        assertThat(ex.getErrorResponse().getCode()).isEqualTo(ErrorResponseCode.ECRITICAUTH_08.getCode());
    }

    @Test
    void givenInvalidPassword_thenThrowBusinessViolationException() {
        User user = UserFixture.load();

        when(findUserBoundary.execute(any(), any())).thenReturn(Optional.of(user));
        when(bcrypt.matches(any(), any())).thenReturn(false);

        BusinessViolationException ex = assertThrows(BusinessViolationException.class, () -> signInUserUseCase.execute("email", "password"));

        verify(findUserBoundary).execute(any(), any());
        verify(bcrypt).matches(any(), any());
        verifyNoInteractions(generateAccessTokenUseCase);
        verifyNoInteractions(generateRefreshTokenUseCase);

        assertThat(ex.getErrorResponse().getCode()).isEqualTo(ErrorResponseCode.ECRITICAUTH_07.getCode());
    }
}