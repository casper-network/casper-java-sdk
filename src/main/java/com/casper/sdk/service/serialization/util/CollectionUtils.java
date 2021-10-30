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

        /**
         * Returns an ordered list containing the provided elements
         *
         * @param elements the elements to add to the list
         * @param <T>      the type of the elements
         * @return a new set
         */
        @SafeVarargs
        public static <T> java.util.List<T> of(final T... elements) {
            return elements != null ? Arrays.asList(elements) : Collections.emptyList();
        }
    }

    public static class Map {
        /**
         * Returns a map containing the provided key pairs.
         *
         * @param keyValuePairs the key pairs as an array with a key followed by a value
         * @param <K>           the key type
         * @param <V>           the value type
         * @return a new R map
         */
        @SuppressWarnings("unchecked")
        public static <K, V> java.util.Map<K, V> of(final Object... keyValuePairs) {
            if (keyValuePairs != null) {
                final java.util.Map<K, V> map = new LinkedHashMap<>();
                for (int i = 0; i < keyValuePairs.length; i += 2) {
                    map.put((K) keyValuePairs[i], (V) keyValuePairs[i + 1]);
                }
                return map;
            } else {
                return Collections.emptyMap();
            }
        }
    }

    public static class Set {

        /**
         * Returns an ordered set containing the provided elements
         *
         * @param elements the elements to add to the set
         * @param <T>      the type of the elements
         * @return a new set
         */
        @SafeVarargs
        public static <T> java.util.Set<T> of(final T... elements) {
            if (elements != null) {
                final java.util.Set<T> set = new LinkedHashSet<>();
                Collections.addAll(set, elements);
                return set;
            } else {
                return Collections.emptySet();
            }
        }
    }

    /**
     * Utility method to suppress Java's stupid generics warnings
     *
     * @param obj the object to cast to a generic type
     * @param <T> the type to cast to
     * @return the cast object
     */
    @SuppressWarnings("unchecked")
    public static <T> T genericCast(final Object obj) {
        return (T) obj;
    }
}
