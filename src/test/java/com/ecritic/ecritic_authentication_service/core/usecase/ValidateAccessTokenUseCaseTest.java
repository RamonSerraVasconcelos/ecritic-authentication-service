package com.ecritic.ecritic_authentication_service.core.usecase;

import com.ecritic.ecritic_authentication_service.config.properties.ApplicationProperties;
import com.ecritic.ecritic_authentication_service.core.fixture.TokenFixture;
import com.ecritic.ecritic_authentication_service.core.model.AccessToken;
import com.ecritic.ecritic_authentication_service.core.model.Token;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.CheckBlacklistedUserBoundary;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.ValidateJwtTokenBoundary;
import com.ecritic.ecritic_authentication_service.dataprovider.jwt.fixture.JwtFixture;
import com.ecritic.ecritic_authentication_service.exception.UnauthorizedAccessException;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.SecretKey;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateAccessTokenUseCaseTest {

    @InjectMocks
    private ValidateAccessTokenUseCase validateAccessTokenUseCase;

    @Mock
    private ValidateJwtTokenBoundary validateJwtTokenBoundary;

    @Mock
    private CheckBlacklistedUserBoundary checkBlacklistedUserBoundary;

    @Mock
    private ApplicationProperties applicationProperties;

    @Test
    void givenExecution_validateAndReturnAccessToken() {
        Token token = TokenFixture.load();
        String jwtToken = JwtFixture.loadSignedJwt();

        when(applicationProperties.getJwtSecret()).thenReturn("secret");
        when(validateJwtTokenBoundary.execute(anyString(), any(SecretKey.class))).thenReturn(token);
        when(checkBlacklistedUserBoundary.isUserBlacklisted(any())).thenReturn(false);

        AccessToken accessToken = validateAccessTokenUseCase.execute(jwtToken);

        verify(applicationProperties).getJwtSecret();
        verify(validateJwtTokenBoundary).execute(anyString(), any(SecretKey.class));
        verify(checkBlacklistedUserBoundary).isUserBlacklisted(token.getUser().getId());

        assertThat(accessToken).isNotNull();
        assertThat(accessToken.getId()).isEqualTo(token.getId());
        assertThat(accessToken.getJwt()).isEqualTo(token.getJwt());
        assertThat(accessToken.getIssuer()).isEqualTo(token.getIssuer());
        assertThat(accessToken.getAud()).isEqualTo(token.getAud());
        assertThat(accessToken.getUser()).isEqualTo(token.getUser());
        assertThat(accessToken.getIssuedAt()).isEqualTo(token.getIssuedAt());
        assertThat(accessToken.getExpiresAt()).isEqualTo(token.getExpiresAt());
    }

    @Test
    void givenBlacklistedUser_thenThrowUnauthorizedAccessException() {
        Token token = TokenFixture.load();
        String jwtToken = JwtFixture.loadSignedJwt();

        when(applicationProperties.getJwtSecret()).thenReturn("secret");
        when(validateJwtTokenBoundary.execute(anyString(), any(SecretKey.class))).thenReturn(token);
        when(checkBlacklistedUserBoundary.isUserBlacklisted(any())).thenReturn(true);

        UnauthorizedAccessException ex = assertThrows(UnauthorizedAccessException.class, () -> validateAccessTokenUseCase.execute(jwtToken));

        verify(applicationProperties).getJwtSecret();
        verify(validateJwtTokenBoundary).execute(anyString(), any(SecretKey.class));
        verify(checkBlacklistedUserBoundary).isUserBlacklisted(token.getUser().getId());

        assertThat(ex.getErrorResponse().getCode()).isEqualTo(ErrorResponseCode.ECRITICAUTH_03.getCode());
    }
}