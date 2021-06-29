package com.casper.sdk.service.serialization.cltypes;

import com.casper.sdk.domain.CLType;

import java.util.HashMap;
import java.util.Map;

public class TypesFactory {

    private final Map<CLType, TypesSerializer> instances = new HashMap<>();

    public TypesFactory() {
        instances.put(CLType.BOOL, new BoolSerializer());
        instances.put(CLType.BYTE_ARRAY, new ByteArraySerializer(this));
        instances.put(CLType.KEY, new KeySerializer());
        instances.put(CLType.I32, new I32Serializer());
        instances.put(CLType.I64, new I64Serializer());
        instances.put(CLType.PUBLIC_KEY, new PublicKeySerializer());
        instances.put(CLType.STRING, new StringSerializer(this));
        instances.put(CLType.U32, new U32Serializer());
        instances.put(CLType.U64, new U64Serializer());
        instances.put(CLType.U128, new U128Serializer());
        instances.put(CLType.U256, new U256Serializer());
        instances.put(CLType.U512, new U512Serializer());
        instances.put(CLType.UNIT, new UnitSerializer());
        instances.put(CLType.UREF, new URefSerializer());
    }

    public TypesSerializer getInstance(final CLType inputType) {
        return instances.get(inputType);
    }
}
