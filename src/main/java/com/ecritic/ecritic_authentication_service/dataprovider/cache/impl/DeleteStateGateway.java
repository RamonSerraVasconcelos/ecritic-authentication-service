package com.ecritic.ecritic_authentication_service.dataprovider.cache.impl;

import com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2.DeleteStateBoundary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeleteStateGateway implements DeleteStateBoundary {

    private final Jedis jedis;

    public void execute(String state) {
        log.info("Deleting state: [{}] from cache", state);

        try {
            jedis.del(state);
        } catch (Exception ex) {
            log.error("Error deleting state: [{}]", state, ex);
        }
    }
}
