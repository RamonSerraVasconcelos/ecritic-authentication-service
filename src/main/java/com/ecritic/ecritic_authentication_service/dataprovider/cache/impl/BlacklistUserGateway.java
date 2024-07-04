package com.ecritic.ecritic_authentication_service.dataprovider.cache.impl;

import com.ecritic.ecritic_authentication_service.core.usecase.boundary.BlacklistUserBoundary;
import com.ecritic.ecritic_authentication_service.dataprovider.cache.utils.CacheKeys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class BlacklistUserGateway implements BlacklistUserBoundary {

    private final Jedis jedis;

    public void execute(UUID userId, int expirationDateTimeInSec) {
        try {
            String cacheKey = CacheKeys.USERS_BLACKLIST_KEY.buildKey(userId.toString());

            long expirationDateTimeInMillis = LocalDateTime
                    .ofEpochSecond(expirationDateTimeInSec, 0, ZoneOffset.UTC)
                    .toInstant(ZoneOffset.UTC)
                    .toEpochMilli();

            jedis.set(cacheKey, userId.toString());
            jedis.pexpire(cacheKey, expirationDateTimeInMillis);
        } catch (Exception ex) {
            log.error("Error removing blacklist key for userId: [{}] from cache", userId, ex);
        }
    }
}
