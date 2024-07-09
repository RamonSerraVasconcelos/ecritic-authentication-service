package com.ecritic.ecritic_authentication_service.dataprovider.cache.impl;

import com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2.CacheStateBoundary;
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

    public void execute(String state) {
        log.info("Caching state: [{}]", state);

        try {
            jedis.set(state, state);
            jedis.pexpire(state, TimeUnit.MINUTES.toMillis(5));
        } catch (Exception ex) {
            log.error("Error caching state: [{}] in cache", state, ex);
            throw ex;
        }
    }
}
