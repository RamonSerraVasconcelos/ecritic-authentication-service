package com.ecritic.ecritic_authentication_service.dataprovider.api.impl;

import com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2.FindAuthServerJwksBoundary;
import com.ecritic.ecritic_authentication_service.dataprovider.api.client.AuthorizationClient;
import com.ecritic.ecritic_authentication_service.dataprovider.api.config.AuthServerErrorDecoder;
import com.ecritic.ecritic_authentication_service.exception.BusinessViolationException;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;
import com.nimbusds.jose.jwk.JWKSet;
import feign.Feign;
import feign.Logger;
import feign.Target;
import feign.slf4j.Slf4jLogger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.text.ParseException;

@Component
@RequiredArgsConstructor
@Slf4j
public class FindAuthServerJwksGateway implements FindAuthServerJwksBoundary {

    private final ObjectFactory<HttpMessageConverters> messageConverters;

    private final ObjectProvider<HttpMessageConverterCustomizer> customizers;

    public JWKSet execute(URI jwksUri) {
        try {
            AuthorizationClient authorizationClient = getFeignClient();

            String jwksString = authorizationClient.getJwks(jwksUri);

            return JWKSet.parse(jwksString);
        } catch (ParseException ex) {
            log.error("Error parsing jwks", ex);
            throw new BusinessViolationException(ErrorResponseCode.ECRITICAUTH_12);
        }
    }

    private AuthorizationClient getFeignClient() {
        return Feign.builder()
                .encoder(new SpringEncoder(messageConverters))
                .decoder(new SpringDecoder(messageConverters, customizers))
                .logger(new Slf4jLogger(AuthorizationClient.class))
                .logLevel(Logger.Level.FULL)
                .contract(new SpringMvcContract())
                .errorDecoder(new AuthServerErrorDecoder())
                .target(Target.EmptyTarget.create(AuthorizationClient.class));
    }
}
