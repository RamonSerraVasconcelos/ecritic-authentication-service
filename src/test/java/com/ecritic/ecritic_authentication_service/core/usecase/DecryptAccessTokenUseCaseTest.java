package com.ecritic.ecritic_authentication_service.core.usecase;

import com.ecritic.ecritic_authentication_service.core.fixture.AccessTokenFixture;
import com.ecritic.ecritic_authentication_service.core.model.AccessToken;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.GenerateUnsignedJwtTokenBoundary;
import com.ecritic.ecritic_authentication_service.dataprovider.jwt.fixture.JwtFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DecryptAccessTokenUseCaseTest {

    @InjectMocks
    private DecryptAccessTokenUseCase decryptAccessTokenUseCase;

    @Mock
    private ValidateAccessTokenUseCase validateAccessTokenUseCase;

    @Mock
    private GenerateUnsignedJwtTokenBoundary generateUnsignedJwtTokenBoundary;

    @Test
    void givenSignedJwt_thenGenerate_andReturnUnsignedJwt() {
        AccessToken accessToken = AccessTokenFixture.load();
        String signedJwt = JwtFixture.loadSignedJwt();
        String unsignedJwt = JwtFixture.loadUnsignedJwt();

        when(validateAccessTokenUseCase.execute(signedJwt)).thenReturn(accessToken);
        when(generateUnsignedJwtTokenBoundary.execute(accessToken)).thenReturn(unsignedJwt);

        AccessToken resultToken = decryptAccessTokenUseCase.execute(signedJwt);

        verify(validateAccessTokenUseCase).execute(signedJwt);
        verify(generateUnsignedJwtTokenBoundary).execute(accessToken);

        assertThat(resultToken.getJwt()).isEqualTo(unsignedJwt);
    }
}