package com.ecritic.ecritic_authentication_service.dataprovider.jwt.impl;

import com.ecritic.ecritic_authentication_service.core.model.Token;
import com.ecritic.ecritic_authentication_service.core.model.User;
import com.ecritic.ecritic_authentication_service.core.model.enums.Role;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.ValidateJwtTokenBoundary;
import com.ecritic.ecritic_authentication_service.exception.UnauthorizedAccessException;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class ValidateJwtTokenGateway implements ValidateJwtTokenBoundary {

    public Token execute(String jwtToken, SecretKey secretKey) {
        JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();

        try {
            Claims claims = jwtParser.parseSignedClaims(jwtToken).getPayload();

            return Token.builder()
                    .id(getId(claims))
                    .jwt(jwtToken)
                    .issuer(getIssuer(claims))
                    .aud(getAud(claims))
                    .user(User.builder()
                            .id(getUserId(claims))
                            .role(getUserRole(claims))
                            .build())
                    .issuedAt(getIssuedAt(claims))
                    .expiresAt(getExpiresAt(claims))
                    .build();
        } catch (SecurityException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        throw new UnauthorizedAccessException(ErrorResponseCode.ECRITICAUTH_03);
    }

    private UUID getId(Claims claims) {
        return UUID.fromString(claims.getId());
    }

    private String getIssuer(Claims claims) {
        return claims.getIssuer();
    }

    private Set<String> getAud(Claims claims) {
        return claims.getAudience();
    }

    private UUID getUserId(Claims claims) {
        return UUID.fromString(claims.get("userId", String.class));
    }

    private Role getUserRole(Claims claims) {
        return Role.valueOf(claims.get("userRole", String.class));
    }

    private LocalDateTime getIssuedAt(Claims claims) {
        return claims.getIssuedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private LocalDateTime getExpiresAt(Claims claims) {
        return claims.getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
