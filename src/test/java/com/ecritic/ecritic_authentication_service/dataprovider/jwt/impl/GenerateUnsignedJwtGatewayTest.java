package com.ecritic.ecritic_authentication_service.dataprovider.jwt.impl;

import com.ecritic.ecritic_authentication_service.core.fixture.RefreshTokenFixture;
import com.ecritic.ecritic_authentication_service.core.model.RefreshToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class GenerateUnsignedJwtGatewayTest {

    @InjectMocks
    private GenerateUnsignedJwtGateway generateUnsignedJwtGateway;

    @Test
    void givenExecution_thenGenerate_andReturnUnsignedJwtToken() {
        RefreshToken refreshToken = RefreshTokenFixture.load();

        String jwtToken = generateUnsignedJwtGateway.execute(refreshToken);

        JwtParser jwtParser = Jwts.parser().unsecured().build();
        Claims claims = jwtParser.parseUnsecuredClaims(jwtToken).getPayload();

        assertThat(jwtToken).isNotEmpty();
        assertThat(claims.getId()).isEqualTo(refreshToken.getId().toString());
        assertThat(claims.getIssuer()).isEqualTo(refreshToken.getIssuer());
        assertThat(claims.getAudience()).isEqualTo(refreshToken.getAud());
        assertThat(UUID.fromString(claims.get("userId").toString())).isEqualTo(refreshToken.getUser().getId());
        assertThat(claims.get("userRole")).isEqualTo(refreshToken.getUser().getRole().name());
        assertThat(claims.getIssuedAt().toInstant().truncatedTo(ChronoUnit.MINUTES))
                .isEqualTo(refreshToken.getIssuedAt().atZone(ZoneId.systemDefault()).toInstant().truncatedTo(ChronoUnit.MINUTES));
        assertThat(claims.getExpiration().toInstant().truncatedTo(ChronoUnit.MINUTES))
                .isEqualTo(refreshToken.getExpiresAt().atZone(ZoneId.systemDefault()).toInstant().truncatedTo(ChronoUnit.MINUTES));
    }
}