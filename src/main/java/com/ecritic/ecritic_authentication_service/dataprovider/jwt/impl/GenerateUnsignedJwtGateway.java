package com.ecritic.ecritic_authentication_service.dataprovider.jwt.impl;

import com.ecritic.ecritic_authentication_service.core.model.Token;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.GenerateUnsignedJwtTokenBoundary;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class GenerateUnsignedJwtGateway implements GenerateUnsignedJwtTokenBoundary {

    public String execute(Token token) {
        return Jwts.builder()
                .id(token.getId().toString())
                .claim("userId", token.getUser().getId().toString())
                .claim("userRole", token.getUser().getRole().name())
                .issuer(token.getIssuer())
                .audience()
                    .add(token.getAud())
                    .and()
                .issuedAt(Date.from(token.getIssuedAt().atZone(ZoneId.systemDefault()).toInstant()))
                .expiration(Date.from(token.getExpiresAt().atZone(ZoneId.systemDefault()).toInstant()))
                .compact();
    }
}
