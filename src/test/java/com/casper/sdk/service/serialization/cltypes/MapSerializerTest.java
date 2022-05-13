package com.casper.sdk.service.serialization.cltypes;

import com.casper.sdk.service.serialization.util.ByteUtils;
import com.casper.sdk.types.*;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Unit tests for the {@link MapSerializer}
 */
class MapSerializerTest {

    private final MapSerializer serializer = new MapSerializer(new TypesFactory());

    @Test
    void serializeCLMap() {

        final byte[] expected = ByteUtils.decodeHex(
                "02000000e3d394334ce46c6043bcd33e4686d2b7a369c606bfcce4c26ca14d2c73fac82403801a06219ac9a617de3433d6ab1c9fa4aa9fb8d874dba9a00b2b562d16da533460657503c02709"
        );

        final String hexKey1 = "e3D394334Ce46C6043BCd33E4686D2B7a369C606BfCce4C26ca14d2C73Fac824";
        final String hexKey2 = "219ac9a617DE3433d6ab1C9fA4aa9FB8D874DBa9A00b2B562d16da5334606575";

        final CLMap clMap = new CLMap((byte[]) null,
                new CLMapTypeInfo(new CLByteArrayInfo(32), new CLTypeInfo(CLType.U256)),
                Map.of(

                        new CLValue(ByteUtils.decodeHex(hexKey1), new CLByteArrayInfo(32), hexKey1),
                        CLValueBuilder.u256("400000"),
                        new CLValue(ByteUtils.decodeHex(hexKey2), new CLByteArrayInfo(32), hexKey2),
                        CLValueBuilder.u256("600000")
                )
        );

        assertThat(serializer.serialize(clMap), is(expected));
    }
}