package com.casper.sdk.service.serialization.factory;

import java.util.HashMap;
import java.util.Map;

public class TypesFactory {

    private final Map<String, TypesInterface> instances = new HashMap<>();

    public TypesFactory() {
        instances.put(TypesEnum.U512.name(), new U512());
        instances.put(TypesEnum.U64.name(), new U64());
        instances.put(TypesEnum.U32.name(), new U32());
        instances.put(TypesEnum.String.name(), new StringType());
    }
    public TypesInterface getInstance(String inputType) {
        return instances.get(inputType);
    }

}
