package com.ecritic.ecritic_authentication_service.core.usecase;

import com.ecritic.ecritic_authentication_service.config.properties.ApplicationProperties;
import com.ecritic.ecritic_authentication_service.core.fixture.UserFixture;
import com.ecritic.ecritic_authentication_service.core.model.AccessToken;
import com.ecritic.ecritic_authentication_service.core.model.User;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.GenerateJwtTokenBoundary;
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
class GenerateAccessTokenUseCaseTest {

    @InjectMocks
    private GenerateAccessTokenUseCase generateAccessTokenUseCase;

    @Mock
    private GenerateJwtTokenBoundary generateJwtTokenBoundary;

    @Mock
    private ApplicationProperties applicationProperties;

    @Test
    void givenExecution_thenGenerate_andReturnAccessToken() {
        User user = UserFixture.load();
        String signedJwt = JwtFixture.loadSignedJwt();

        when(applicationProperties.getJwtSecret()).thenReturn("secret");
        when(generateJwtTokenBoundary.execute(any(AccessToken.class), any(SecretKey.class))).thenReturn(signedJwt);

        AccessToken accessToken = generateAccessTokenUseCase.execute(user);

        verify(generateJwtTokenBoundary).execute(any(AccessToken.class), any(SecretKey.class));

        assertThat(accessToken).isNotNull();
        assertThat(accessToken.getId()).isNotNull();
        assertThat(accessToken.getIssuer()).isEqualTo("ecritic");
        assertThat(accessToken.getAud()).isEqualTo(Set.of("ecritic"));
        assertThat(accessToken.getUser().getId()).isEqualTo(user.getId());
        assertThat(accessToken.getUser().getRole()).isEqualTo(user.getRole());
        assertThat(accessToken.getJwt()).isEqualTo(signedJwt);
        assertThat(accessToken.getIssuedAt()).isNotNull();
        assertThat(accessToken.getExpiresAt()).isNotNull();
    }
}