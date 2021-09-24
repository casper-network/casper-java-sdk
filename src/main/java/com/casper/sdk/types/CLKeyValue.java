package com.casper.sdk.types;

import com.casper.sdk.service.json.serialize.CLKeyValueJsonSerializer;
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

    public CLKeyInfo.KeyType getKeyType() {
        return ((CLKeyInfo)getCLTypeInfo()).getKeyType();
    }
}
