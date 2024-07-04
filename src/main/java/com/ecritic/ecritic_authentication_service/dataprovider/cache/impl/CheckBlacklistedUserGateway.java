package com.ecritic.ecritic_authentication_service.dataprovider.cache.impl;

import com.ecritic.ecritic_authentication_service.core.usecase.boundary.CheckBlacklistedUserBoundary;
import com.ecritic.ecritic_authentication_service.dataprovider.cache.utils.CacheKeys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class CheckBlacklistedUserGateway implements CheckBlacklistedUserBoundary {

    private final Jedis jedis;

    public boolean isUserBlacklisted(UUID userId) {
        return jedis.exists(CacheKeys.USERS_BLACKLIST_KEY.buildKey(userId.toString()));
    }
}
