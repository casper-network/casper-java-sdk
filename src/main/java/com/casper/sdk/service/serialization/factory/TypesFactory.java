package com.casper.sdk.service.serialization.factory;

import com.casper.sdk.domain.CLType;

import java.util.HashMap;
import java.util.Map;

public class TypesFactory {

    private final Map<CLType, TypesSerializer> instances = new HashMap<>();

    public TypesFactory() {
        instances.put(CLType.U512, new U512());
        instances.put(CLType.U64, new U64());
        instances.put(CLType.U32, new U32());
        instances.put(CLType.STRING, new StringType(this));
    }

    public TypesSerializer getInstance(final CLType inputType) {
        return instances.get(inputType);
    }
}
