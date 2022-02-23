package com.casper.sdk.service.serialization.cltypes;

import com.casper.sdk.service.serialization.util.ByteArrayBuilder;
import com.casper.sdk.types.CLOptionTypeInfo;
import com.casper.sdk.types.CLOptionValue;
import com.casper.sdk.types.CLTypeInfo;

import java.util.Optional;

public class OptionSerializer implements TypesSerializer {

    private final TypesFactory typesFactory;

    public OptionSerializer(final TypesFactory typesFactory) {
        this.typesFactory = typesFactory;
    }

    @Override
    public byte[] serialize(final Object toSerialize) {

        final ByteArrayBuilder builder = new ByteArrayBuilder();

        if (toSerialize instanceof Optional) {
            return this.serialize(((Optional<?>) toSerialize).orElse(null));
        } else if (toSerialize instanceof CLOptionValue) {
            CLOptionValue clOptionValue = (CLOptionValue) toSerialize;
            if (clOptionValue.getBytes() == null || clOptionValue.getBytes().length == 0) {
                builder.append(CLOptionValue.OPTION_NONE);
            } else {
                builder.append(CLOptionValue.OPTION_SOME);
                final CLTypeInfo innerType = ((CLOptionTypeInfo) clOptionValue.getCLTypeInfo()).getInnerType();
                builder.append(typesFactory.getInstance(innerType.getType()).serialize(clOptionValue.getParsed()));
            }
        }
        return builder.toByteArray();
    }
}
