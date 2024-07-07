package com.ecritic.ecritic_authentication_service.core.usecase;

import com.ecritic.ecritic_authentication_service.config.properties.ApplicationProperties;
import com.ecritic.ecritic_authentication_service.core.fixture.UserFixture;
import com.ecritic.ecritic_authentication_service.core.model.RefreshToken;
import com.ecritic.ecritic_authentication_service.core.model.User;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.GenerateJwtTokenBoundary;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.SaveRefreshTokenBoundary;
import com.ecritic.ecritic_authentication_service.dataprovider.jwt.fixture.JwtFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.SecretKey;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenerateRefreshTokenUseCaseTest {

    @InjectMocks
    private GenerateRefreshTokenUseCase generateRefreshTokenUseCase;

    @Mock
    private GenerateJwtTokenBoundary generateJwtTokenBoundary;

    @Mock
    private SaveRefreshTokenBoundary saveRefreshTokenBoundary;

    @Mock
    private ApplicationProperties applicationProperties;

    @Test
    void givenExecution_thenGenerate_andReturnRefreshToken() {
        User user = UserFixture.load();

        when(applicationProperties.getJwtRefreshSecret()).thenReturn("secret");
        when(generateJwtTokenBoundary.execute(any(RefreshToken.class), any(SecretKey.class))).thenReturn(JwtFixture.loadSignedJwt());

        RefreshToken refreshToken = generateRefreshTokenUseCase.execute(user);

        verify(generateJwtTokenBoundary).execute(any(RefreshToken.class), any(SecretKey.class));

        assertThat(refreshToken).isNotNull();
        assertThat(refreshToken.getId()).isNotNull();
        assertThat(refreshToken.getIssuer()).isEqualTo("ecritic");
        assertThat(refreshToken.getAud()).isEqualTo(Set.of("ecritic"));
        assertThat(refreshToken.getUser().getId()).isEqualTo(user.getId());
        assertThat(refreshToken.getUser().getRole()).isEqualTo(user.getRole());
        assertThat(refreshToken.isActive()).isTrue();
        assertThat(refreshToken.getJwt()).isEqualTo(JwtFixture.loadSignedJwt());
        assertThat(refreshToken.getIssuedAt()).isNotNull();
        assertThat(refreshToken.getExpiresAt()).isNotNull();
    }
}