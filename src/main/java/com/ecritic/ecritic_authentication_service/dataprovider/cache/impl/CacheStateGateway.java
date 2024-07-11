package com.ecritic.ecritic_authentication_service.dataprovider.cache.impl;

import com.ecritic.ecritic_authentication_service.core.model.AuthorizationRequest;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2.CacheStateBoundary;
import com.ecritic.ecritic_authentication_service.exception.InternalErrorException;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class CacheStateGateway implements CacheStateBoundary {

    private final Jedis jedis;

    private final ObjectMapper objectMapper;

    public void execute(String state, AuthorizationRequest authorizationRequest) {
        log.info("Caching state: [{}]", state);

        try {
            String authorizationRequestJson = objectMapper.writeValueAsString(authorizationRequest);

            jedis.set(state, authorizationRequestJson);
            jedis.pexpire(state, TimeUnit.MINUTES.toMillis(5));
        } catch (Exception ex) {
            log.error("Error caching state: [{}] in cache", state, ex);
            throw new InternalErrorException(ErrorResponseCode.ECRITICAUTH_11);
        }
    }
}
