package com.dimosr.cache;

import com.google.common.cache.Cache;

import java.util.Optional;

public class GuavaCache<K, V> implements com.dimosr.service.core.Cache<K, V> {
    private Cache<K, V> guavaCache;

    public GuavaCache(final Cache<K, V> guavaCache) {
        this.guavaCache = guavaCache;
    }

    @Override
    public Optional<V> get(K key) {
        return Optional.ofNullable(guavaCache.getIfPresent(key));
    }

    @Override
    public void put(K key, V value) {
        guavaCache.put(key, value);
    }
}
