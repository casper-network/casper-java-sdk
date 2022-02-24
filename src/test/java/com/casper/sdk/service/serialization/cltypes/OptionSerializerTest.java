package com.casper.sdk.service.serialization.cltypes;

import com.casper.sdk.types.*;
import org.junit.jupiter.api.Test;

import static com.casper.sdk.service.serialization.cltypes.CLValueBuilder.u32;
import static com.casper.sdk.service.serialization.util.ByteUtils.concat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class OptionSerializerTest {

    private final OptionSerializer optionSerializer = new OptionSerializer(new TypesFactory());

    @Test
    void optionalSomeWithParsedToBytes() {

        final CLValue version = u32(1234);

        final CLOptionValue clOptionValue = new CLOptionValue(
                version.getBytes(),
                new CLOptionTypeInfo(new CLTypeInfo(CLType.U32)),
                version.getParsed()
        );

        final byte[] bytes = optionSerializer.serialize(clOptionValue);
        assertThat(bytes, is(concat(CLOptionValue.OPTION_SOME, version.getBytes())));
    }

    @Test
    void optionalSomeWithoutParsedToBytes() {

        final CLValue version = u32(1234);

        final CLOptionValue clOptionValue = new CLOptionValue(
                version.getBytes(),
                new CLOptionTypeInfo(new CLTypeInfo(CLType.U32)),
                null
        );

        final byte[] bytes = optionSerializer.serialize(clOptionValue);
        assertThat(bytes, is(concat(CLOptionValue.OPTION_SOME, version.getBytes())));
    }

    @Test
    void optionalNoneToBytes() {

        final CLOptionValue clOptionValue = new CLOptionValue(
                new byte[0],
                new CLOptionTypeInfo(new CLTypeInfo(CLType.U32)),
                null
        );

        final byte[] bytes = optionSerializer.serialize(clOptionValue);
        assertThat(bytes, is(CLOptionValue.OPTION_NONE));
    }
}