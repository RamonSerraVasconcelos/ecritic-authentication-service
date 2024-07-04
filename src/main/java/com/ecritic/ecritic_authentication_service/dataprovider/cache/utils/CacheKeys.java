package com.ecritic.ecritic_authentication_service.dataprovider.cache.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CacheKeys {

    USERS_BLACKLIST_KEY("users:blacklist:%s");

    private final String key;

    public String buildKey(String... replacements) {
        return String.format(key, (Object[]) replacements);
    }
}
