package com.ecritic.ecritic_authentication_service.dataprovider.api.impl;

import com.ecritic.ecritic_authentication_service.core.model.AuthorizationRequest;
import com.ecritic.ecritic_authentication_service.core.model.AuthorizationServer;
import com.ecritic.ecritic_authentication_service.core.model.ExternalToken;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2.GenerateExternalTokenBoundary;
import com.ecritic.ecritic_authentication_service.dataprovider.api.client.AuthorizationClient;
import com.ecritic.ecritic_authentication_service.dataprovider.api.entity.ExternalTokenEntity;
import com.ecritic.ecritic_authentication_service.dataprovider.api.interpector.AuthorizationInterceptor;
import com.ecritic.ecritic_authentication_service.dataprovider.api.mapper.ExternalTokenEntityMapper;
import feign.Feign;
import feign.Target;
import lombok.RequiredArgsConstructor;
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
@Import(FeignClientsConfiguration.class)
public class GenerateExternalTokenGateway implements GenerateExternalTokenBoundary {

    private final Feign.Builder feignBuilder;

    private final ObjectFactory<HttpMessageConverters> messageConverters;

    private final ObjectProvider<HttpMessageConverterCustomizer> customizers;

    private final ExternalTokenEntityMapper externalTokenEntityMapper;

    private static final String GRANT_TYPE = "authorization_code";

    public ExternalToken execute(AuthorizationRequest authorizationRequest, AuthorizationServer authorizationServer, String code) {
        AuthorizationClient authorizationClient = getFeignClient(authorizationServer.getTokenEndpoint().toString());

        Map<String, ?> params = buildParams(authorizationRequest, authorizationServer, code);

        ExternalTokenEntity externalTokenEntity = authorizationClient.generateExternalToken(authorizationRequest.getRedirectUri(), params);

        return externalTokenEntityMapper.externalTokenEntityToExternalToken(externalTokenEntity);
    }

    public AuthorizationClient getFeignClient(String baseUrl) {
        return feignBuilder.
                requestInterceptor(new AuthorizationInterceptor(() -> baseUrl))
                .contract(new SpringMvcContract())
                .encoder(new SpringEncoder(messageConverters))
                .decoder(new SpringDecoder(messageConverters, customizers))
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
