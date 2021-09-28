package com.casper.sdk.types;

import com.casper.sdk.service.json.serialize.CLKeyValueJsonSerializer;
import com.casper.sdk.service.serialization.util.ByteUtils;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Specialized value type for keys
 */
@JsonSerialize(using = CLKeyValueJsonSerializer.class)
public class CLKeyValue extends CLValue {

    public CLKeyValue(final String hexBytes, final CLKeyInfo.KeyType keyType, final Object parsed) {
        super(hexBytes, new CLKeyInfo(keyType), parsed);
    }

    public CLKeyValue(final byte[] bytes, final CLKeyInfo.KeyType keyType, final Object parsed) {
        super(bytes, new CLKeyInfo(keyType), parsed);
    }

    /**
     * Obtains the bytes including the key type prefix byte
     *
     * @return the bytes
     */
    @Override
    public byte[] getBytes() {
        return ByteUtils.concat(ByteUtils.toByteArray(getKeyType().getTag()), super.getBytes());
    }

    /**
     * Obtains the bytes without the key type prefix
     *
     * @return the original key bytes without the key type prefix byte
     */
    public byte[] getKeyBytes() {
        return super.getBytes();
    }

    public CLKeyInfo.KeyType getKeyType() {
        return ((CLKeyInfo) getCLTypeInfo()).getKeyType();
    }
}
