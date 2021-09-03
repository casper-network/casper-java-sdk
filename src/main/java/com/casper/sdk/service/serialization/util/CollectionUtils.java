package com.casper.sdk.service.serialization.util;


import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * Provide Java 15 compatible collection methods to allow back porting and forward porting between JDKs
 */
public class CollectionUtils {

    public static class List {
        @SafeVarargs
        public static <T> java.util.List<T> of(T... elements) {
            return elements != null ? Arrays.asList(elements) : Collections.emptyList();
        }
    }

    public static class Map {
        public static <K, V> java.util.Map<K, V> of(Object... keyValuePairs) {
            if (keyValuePairs != null) {
                final java.util.Map<K, V> map = new LinkedHashMap<>();
                for (int i = 0; i < keyValuePairs.length; i += 2) {
                    //noinspection unchecked
                    map.put((K) keyValuePairs[i], (V) keyValuePairs[i + 1]);
                }
                return map;
            } else {
                return Collections.emptyMap();
            }
        }
    }

    public static class Set {
        @SafeVarargs
        public static <T> java.util.Set<T> of(T... elements) {
            if (elements != null) {
                final java.util.Set<T> set = new LinkedHashSet<>();
                Collections.addAll(set, elements);
                return set;
            } else {
                return Collections.emptySet();
            }
        }

    }
}
