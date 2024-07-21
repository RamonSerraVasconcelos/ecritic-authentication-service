package com.ecritic.ecritic_authentication_service.dataprovider.api.impl;

import com.ecritic.ecritic_authentication_service.core.model.AuthorizationRequest;
import com.ecritic.ecritic_authentication_service.core.model.AuthorizationServer;
import com.ecritic.ecritic_authentication_service.core.model.ExternalToken;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2.GenerateExternalTokenBoundary;
import com.ecritic.ecritic_authentication_service.dataprovider.api.client.AuthorizationClient;
import com.ecritic.ecritic_authentication_service.dataprovider.api.config.AuthServerErrorDecoder;
import com.ecritic.ecritic_authentication_service.dataprovider.api.entity.ExternalTokenEntity;
import com.ecritic.ecritic_authentication_service.dataprovider.api.mapper.ExternalTokenApiEntityMapper;
import com.ecritic.ecritic_authentication_service.exception.BusinessViolationException;
import com.ecritic.ecritic_authentication_service.exception.ClientException;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;
import feign.Feign;
import feign.Logger;
import feign.Target;
import feign.slf4j.Slf4jLogger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
@Import(FeignClientsConfiguration.class)
public class GenerateExternalTokenGateway implements GenerateExternalTokenBoundary {

    private final ObjectFactory<HttpMessageConverters> messageConverters;

    private final ObjectProvider<HttpMessageConverterCustomizer> customizers;

    private final ExternalTokenApiEntityMapper externalTokenApiEntityMapper;

    private static final String GRANT_TYPE = "authorization_code";

    public ExternalToken execute(AuthorizationRequest authorizationRequest, AuthorizationServer authorizationServer, String code) {
        try {
            AuthorizationClient authorizationClient = getFeignClient();

            Map<String, ?> params = buildParams(authorizationRequest, authorizationServer, code);

            ExternalTokenEntity externalTokenEntity = authorizationClient.generateExternalToken(authorizationServer.getTokenEndpoint(), params);

            return externalTokenApiEntityMapper.externalTokenEntityToExternalToken(externalTokenEntity);
        } catch (ClientException ex) {
            log.error("Error while making request to Authorization Server. Error: [{}]", ex.getResponse());
            throw new BusinessViolationException(ErrorResponseCode.ECRITICAUTH_13);
        } catch (Exception ex) {
            log.error("Error while building request to Authorization Server", ex);
            throw ex;
        }
    }

    public AuthorizationClient getFeignClient() {
        return Feign.builder()
                .encoder(new SpringEncoder(messageConverters))
                .decoder(new SpringDecoder(messageConverters, customizers))
                .logLevel(Logger.Level.FULL)
                .logger(new Slf4jLogger(AuthorizationClient.class))
                .contract(new SpringMvcContract())
                .errorDecoder(new AuthServerErrorDecoder())
                .target(Target.EmptyTarget.create(AuthorizationClient.class));
    }

    private Map<String, ?> buildParams(AuthorizationRequest authorizationRequest, AuthorizationServer authorizationServer, String code) {
        return Map.of(
                "client_id", authorizationRequest.getClientId(),
                "client_secret", authorizationServer.getClientSecret(),
                "grant_type", GRANT_TYPE,
                "code", code,
                "redirect_uri", authorizationRequest.getRedirectUri()
        );
    }
}
