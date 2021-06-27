package com.casper.sdk.service.serialization.cltypes;

import com.casper.sdk.domain.CLType;

abstract class AbstractTypesSerializer implements TypesSerializer {

    private final TypesFactory typesFactory;
    private TypesSerializer u32Serializer;

    AbstractTypesSerializer(TypesFactory typesFactory) {
        this.typesFactory = typesFactory;
    }

    TypesSerializer getU32Serializer() {
        if (u32Serializer == null) {
            this.u32Serializer = typesFactory.getInstance(CLType.U32);
        }
        return u32Serializer;
    }
}
