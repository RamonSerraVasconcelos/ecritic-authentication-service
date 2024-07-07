package com.ecritic.ecritic_authentication_service.core.usecase;

import com.ecritic.ecritic_authentication_service.config.properties.ApplicationProperties;
import com.ecritic.ecritic_authentication_service.core.fixture.RefreshTokenFixture;
import com.ecritic.ecritic_authentication_service.core.fixture.TokenFixture;
import com.ecritic.ecritic_authentication_service.core.model.RefreshToken;
import com.ecritic.ecritic_authentication_service.core.model.Token;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.FindRefreshTokenByIdBoundary;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.ValidateJwtTokenBoundary;
import com.ecritic.ecritic_authentication_service.dataprovider.jwt.fixture.JwtFixture;
import com.ecritic.ecritic_authentication_service.exception.UnauthorizedAccessException;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateRefreshTokenUseCaseTest {

    @InjectMocks
    private ValidateRefreshTokenUseCase validateRefreshTokenUseCase;

    @Mock
    private ValidateJwtTokenBoundary validateJwtTokenBoundary;

    @Mock
    private FindRefreshTokenByIdBoundary findRefreshTokenByIdBoundary;

    @Mock
    private ApplicationProperties applicationProperties;

    @Test
    void givenValidJwtToken_thenValidate_andReturnRefreshToken() {
        Token token = TokenFixture.load();

        RefreshToken refreshToken = RefreshTokenFixture.load();
        refreshToken.setId(token.getId());
        refreshToken.setUser(token.getUser());

        when(applicationProperties.getJwtRefreshSecret()).thenReturn("secret");
        when(validateJwtTokenBoundary.execute(anyString(), any(SecretKey.class))).thenReturn(token);
        when(findRefreshTokenByIdBoundary.execute(any(UUID.class))).thenReturn(Optional.of(refreshToken));

        RefreshToken resultToken = validateRefreshTokenUseCase.execute(JwtFixture.loadSignedJwt());

        verify(applicationProperties).getJwtRefreshSecret();
        verify(validateJwtTokenBoundary).execute(anyString(), any(SecretKey.class));
        verify(findRefreshTokenByIdBoundary).execute(any(UUID.class));

        assertThat(resultToken).isNotNull().usingRecursiveComparison().isEqualTo(refreshToken);
    }

    @Test
    void givenExecution_whenTokenIsNotFound_thenThrowUnauthorizedAccessException() {
        Token token = TokenFixture.load();

        when(applicationProperties.getJwtRefreshSecret()).thenReturn("secret");
        when(validateJwtTokenBoundary.execute(anyString(), any(SecretKey.class))).thenReturn(token);
        when(findRefreshTokenByIdBoundary.execute(any(UUID.class))).thenReturn(Optional.empty());

        UnauthorizedAccessException ex = assertThrows(UnauthorizedAccessException.class, () -> validateRefreshTokenUseCase.execute(JwtFixture.loadSignedJwt()));

        verify(applicationProperties).getJwtRefreshSecret();
        verify(validateJwtTokenBoundary).execute(anyString(), any(SecretKey.class));
        verify(findRefreshTokenByIdBoundary).execute(any(UUID.class));

        assertThat(ex.getErrorResponse().getCode()).isEqualTo(ErrorResponseCode.ECRITICAUTH_03.getCode());
    }

    @Test
    void givenInactiveToken_thenThrowUnauthorizedAccessException() {
        Token token = TokenFixture.load();
        RefreshToken refreshToken = RefreshTokenFixture.load();
        refreshToken.setActive(false);

        when(applicationProperties.getJwtRefreshSecret()).thenReturn("secret");
        when(validateJwtTokenBoundary.execute(anyString(), any(SecretKey.class))).thenReturn(token);
        when(findRefreshTokenByIdBoundary.execute(any(UUID.class))).thenReturn(Optional.of(refreshToken));

        UnauthorizedAccessException ex = assertThrows(UnauthorizedAccessException.class, () -> validateRefreshTokenUseCase.execute(JwtFixture.loadSignedJwt()));

        verify(applicationProperties).getJwtRefreshSecret();
        verify(validateJwtTokenBoundary).execute(anyString(), any(SecretKey.class));
        verify(findRefreshTokenByIdBoundary).execute(any(UUID.class));

        assertThat(ex.getErrorResponse().getCode()).isEqualTo(ErrorResponseCode.ECRITICAUTH_03.getCode());
    }

    @ParameterizedTest
    @MethodSource("provideRefreshTokenData")
    void givenInvalidRefreshToken_thenThrowUnauthorizedAccessException(RefreshToken refreshToken) {
        Token token = TokenFixture.load();

        when(applicationProperties.getJwtRefreshSecret()).thenReturn("secret");
        when(validateJwtTokenBoundary.execute(anyString(), any(SecretKey.class))).thenReturn(token);
        when(findRefreshTokenByIdBoundary.execute(any(UUID.class))).thenReturn(Optional.of(refreshToken));

        UnauthorizedAccessException ex = assertThrows(UnauthorizedAccessException.class, () -> validateRefreshTokenUseCase.execute(JwtFixture.loadSignedJwt()));

        verify(applicationProperties).getJwtRefreshSecret();
        verify(validateJwtTokenBoundary).execute(anyString(), any(SecretKey.class));
        verify(findRefreshTokenByIdBoundary).execute(any(UUID.class));

        assertThat(ex.getErrorResponse().getCode()).isEqualTo(ErrorResponseCode.ECRITICAUTH_03.getCode());
    }

    static Stream<Arguments> provideRefreshTokenData() {
        Token token = TokenFixture.load();

        RefreshToken refreshToken1 = RefreshTokenFixture.load();
        refreshToken1.setIssuer("Invalid issuer");
        refreshToken1.setId(token.getId());
        refreshToken1.setUser(token.getUser());

        RefreshToken refreshToken2 = RefreshTokenFixture.load();
        refreshToken2.setAud(Set.of("Invalid audience"));
        refreshToken2.setId(token.getId());
        refreshToken2.setUser(token.getUser());

        RefreshToken refreshToken3 = RefreshTokenFixture.load();
        refreshToken3.getUser().setId(UUID.randomUUID());
        refreshToken2.setId(token.getId());

        RefreshToken refreshToken4 = RefreshTokenFixture.load();
        refreshToken1.setId(token.getId());
        refreshToken1.setUser(token.getUser());
        refreshToken4.setIssuedAt(LocalDateTime.now().minusSeconds(5));
        refreshToken4.setExpiresAt(LocalDateTime.now().plusSeconds(5));

        return Stream.of(
                Arguments.of(refreshToken1),
                Arguments.of(refreshToken2),
                Arguments.of(refreshToken3),
                Arguments.of(refreshToken4)
        );
    }
}