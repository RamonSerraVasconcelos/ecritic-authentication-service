package com.ecritic.ecritic_authentication_service.dataprovider.jwt.impl;

import com.ecritic.ecritic_authentication_service.core.model.Token;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.GenerateJwtTokenBoundary;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.ZoneId;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class GenerateJwtTokenGateway implements GenerateJwtTokenBoundary {

    @Override
    public String execute(Token token, SecretKey secretKey) {
        Claims claims = Jwts.claims()
                .add("userId", token.getUser().getId())
                .add("userRole", token.getUser().getRole())
                .build();

        return Jwts.builder()
                .header()
                    .type("JWT")
                    .and()
                .audience()
                    .add(token.getAud())
                    .and()
                .id(token.getId().toString())
                .issuer(token.getIssuer())
                .claims(claims)
                .issuedAt(Date.from(token.getIssuedAt().atZone(ZoneId.systemDefault()).toInstant()))
                .expiration(Date.from(token.getExpiresAt().atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(secretKey)
                .compact();
    }
}
