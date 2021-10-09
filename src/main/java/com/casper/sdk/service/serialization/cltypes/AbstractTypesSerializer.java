package com.casper.sdk.service.serialization.cltypes;

import com.casper.sdk.types.CLType;

abstract class AbstractTypesSerializer implements TypesSerializer {

    private final TypesFactory typesFactory;
    private TypesSerializer u32Serializer;
    private PublicKeySerializer publicKeySerializer;

    AbstractTypesSerializer(final TypesFactory typesFactory) {
        this.typesFactory = typesFactory;
    }

    TypesSerializer getU32Serializer() {
        if (u32Serializer == null) {
            this.u32Serializer = typesFactory.getInstance(CLType.U32);
        }
        return u32Serializer;
    }

    PublicKeySerializer getPublicKeySerializer() {
        if (publicKeySerializer == null) {
            publicKeySerializer = typesFactory.getInstance(CLType.PUBLIC_KEY);
        }
        return publicKeySerializer;
    }
}
