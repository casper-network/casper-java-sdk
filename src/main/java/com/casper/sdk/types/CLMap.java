package com.casper.sdk.types;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Serializes as a list of key-value tuples. There must be a well-defined ordering on the keys, and in the
 * serialization, the pairs are listed in ascending order. This is done to ensure determinism in the serialization, as
 * Map data structures can be unordered.
 */
public class CLMap extends CLValue implements Map<CLValue, CLValue> {

    /** The map ordered map */
    private final Map<CLValue, CLValue> map = new LinkedHashMap<>();

    /** A flag that indicates the map has been modified since creation and will need its bytes rebuilding */
    private boolean modified;

    public CLMap(final String hexBytes, final CLMapTypeInfo typeInfo, final Object parsed) {
        this(fromString(hexBytes), typeInfo, parsed);
    }

    public CLMap(final byte[] bytes, final CLMapTypeInfo typeInfo, final Object parsed) {
        super(bytes, typeInfo, parsed);
        if (parsed instanceof Map) {
            //noinspection unchecked
            map.putAll((Map<? extends CLValue, ? extends CLValue>) parsed);
        }
        this.modified = false;
    }

    public CLValue put(final CLValue key, final CLValue value) {
        this.modified = true;
        return this.map.put(key, value);
    }

    @Override
    public CLValue remove(final Object key) {
        this.modified = true;
        return map.remove(key);
    }

    @Override
    public void putAll(@NotNull final Map<? extends CLValue, ? extends CLValue> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        this.modified = true;
        map.clear();
    }

    @NotNull
    @Override
    public Set<CLValue> keySet() {
        return map.keySet();
    }

    @NotNull
    @Override
    public Collection<CLValue> values() {
        return map.values();
    }

    @NotNull
    @Override
    public Set<Entry<CLValue, CLValue>> entrySet() {
        return map.entrySet();
    }

    @NotNull
    public Iterator<Map.Entry<CLValue, CLValue>> iterator() {
        return map.entrySet().iterator();
    }

    public CLTypeInfo getKeyType() {
        return ((CLMapTypeInfo) getCLTypeInfo()).getKeyType();
    }

    public CLTypeInfo getValueType() {
        return ((CLMapTypeInfo) getCLTypeInfo()).getValueType();
    }

    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(final Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return map.containsValue(value);
    }

    @Override
    public CLValue get(final Object key) {
        return map.get(key);
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(final boolean modified) {
        this.modified = modified;
    }
}
