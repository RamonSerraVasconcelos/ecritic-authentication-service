package com.ecritic.ecritic_authentication_service.dataprovider.cache.impl;

import com.ecritic.ecritic_authentication_service.core.usecase.boundary.UnblacklistUserBoundary;
import com.ecritic.ecritic_authentication_service.dataprovider.cache.utils.CacheKeys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class UnblacklistUserGateway implements UnblacklistUserBoundary {

    private final Jedis jedis;

    public void execute(UUID userId) {
        try {
            String cacheKey = CacheKeys.USERS_BLACKLIST_KEY.buildKey(userId.toString());
            jedis.del(cacheKey);
        } catch (Exception ex) {
            log.error("Error removing blacklist key for userId: [{}]", userId, ex);
        }
    }
}
