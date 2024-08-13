package com.ecritic.ecritic_authentication_service.dataprovider.jwt.impl;

import com.ecritic.ecritic_authentication_service.core.model.IdToken;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2.ValidateIdTokenBoundary;
import com.ecritic.ecritic_authentication_service.exception.BusinessViolationException;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;

@Component
@RequiredArgsConstructor
@Slf4j
public class ValidateIdTokenGateway implements ValidateIdTokenBoundary {

    public IdToken execute(String idTokenJwt, JWKSet jwkSet) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(idTokenJwt);

            verifySignature(signedJWT, jwkSet);

            JWTClaimsSet payload = signedJWT.getJWTClaimsSet();

            IdToken idToken = new IdToken();
            idToken.setIssuer(payload.getIssuer());
            idToken.setAud(new HashSet<>(payload.getAudience()));
            idToken.setIssuedAt(LocalDateTime.from(payload.getIssueTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()));
            idToken.setExpiresAt(LocalDateTime.from(payload.getExpirationTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()));
            idToken.setEmail(payload.getClaim("email").toString());
            idToken.setName(payload.getClaim("name").toString());

            return idToken;
        } catch (ParseException | JOSEException ex) {
            log.error("Error parsing id token", ex);
            throw new BusinessViolationException(ErrorResponseCode.ECRITICAUTH_12);
        } catch (Exception ex) {
            log.error("Error validating id token", ex);
            throw new BusinessViolationException(ErrorResponseCode.ECRITICAUTH_12);
        }
    }

    private void verifySignature(SignedJWT signedJWT, JWKSet jwkSet) throws JOSEException {
        JWSHeader header = signedJWT.getHeader();
        String kid = header.toJSONObject().get("kid").toString();

        JWK jwk = jwkSet.getKeyByKeyId(kid);
        RSAKey rsaKey = (RSAKey) jwk;

        JWSVerifier verifier = new RSASSAVerifier(rsaKey);

        if (!signedJWT.verify(verifier)) {
            log.warn("Invalid IdToken signature");
            throw new BusinessViolationException(ErrorResponseCode.ECRITICAUTH_12);
        }
    }
}
