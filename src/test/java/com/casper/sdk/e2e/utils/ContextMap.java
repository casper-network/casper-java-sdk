package com.casper.sdk.e2e.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A class that wraps a map with a string key that allows for any generic type when putting and getting values.
 *
 * @author ian@meywood.com
 */
public final class ContextMap {

    private final Map<String, Object> map = new LinkedHashMap<>();

    private static ContextMap instance;

    public static synchronized ContextMap getInstance() {
        if (instance == null) {
            instance = new ContextMap();
        }
        return instance;
    }

    private ContextMap() {
        // Not allowed outside this class
    }

    public <V> void put(final String key, V value) {
        map.put(key, value);
    }

    public <V> V get(final String key) {
        //noinspection unchecked
        return (V) map.get(key);
    }

    public <V> V remove(final String key) {
        //noinspection unchecked
        return (V) map.remove(key);
    }

    public void clear() {
        this.map.clear();
    }
}
