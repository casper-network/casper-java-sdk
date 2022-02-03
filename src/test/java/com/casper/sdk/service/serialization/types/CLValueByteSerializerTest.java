package com.casper.sdk.service.serialization.types;

import com.casper.sdk.service.serialization.cltypes.CLValueBuilder;
import com.casper.sdk.service.serialization.cltypes.TypesFactory;
import com.casper.sdk.types.*;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.casper.sdk.service.serialization.util.ByteUtils.concat;
import static com.casper.sdk.service.serialization.util.ByteUtils.decodeHex;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class CLValueByteSerializerTest {

    private final TypesFactory typesFactory = new TypesFactory();
    private final CLValueByteSerializer byteSerializer = new CLValueByteSerializer(typesFactory);

    @Test
    void u64toBytes() {

        final byte[] value = decodeHex("01e703000000000000");
        final byte[] expected = concat(
                typesFactory.getInstance(CLType.U32).serialize(value.length),
                value,
                new byte[]{CLType.U64.getClType()}
        );

        final CLValue source = new CLValue(
                decodeHex("01e703000000000000"),
                CLType.U64,
                "999"
        );

        assertThat(byteSerializer.toBytes(source), is(expected));
    }

    @Test
    void u512ValueToBytes() {


        final CLValue source = new CLValue(
                decodeHex("0500e40b5402"),
                CLType.U512,
                "10000000000"
        );

        final byte[] expected = concat(
                typesFactory.getInstance(CLType.U32).serialize(6),
                decodeHex("0500e40b5402"),
                new byte[]{CLType.U512.getClType()}
        );

        assertThat(byteSerializer.toBytes(source), is(expected));
    }

    @Test
    void optionValueToBytes() {

        final byte[] expected = {
                9,              // LENGTH OF VALUE
                0,
                0,
                0,
                0x01,           // OPTION_SOME
                (byte) 0xa0,    // VALUE START
                (byte) 0x86,
                0x01,
                0x00,
                0x00,
                0x00,
                0x00,
                0x00,           // END OF VALUE
                13,             // OPTION type
                5               // U64 type
        };

        final CLValue optionValue = new CLValue("01a086010000000000", new CLOptionTypeInfo(new CLTypeInfo(CLType.U64)), 100000);

        assertThat(byteSerializer.toBytes(optionValue), is(expected));
    }

    @Test
    void byteOptionArrayKeyValue() {

        final CLValue optionValue = new CLValue("017b7708f6ac895b6748b65fb183cad8c4c62d7bbf6f505455d662b5f691c0f9b2", new CLOptionTypeInfo(new CLByteArrayInfo(32)), "7b7708f6ac895b6748b65fb183cad8c4c62d7bbf6f505455d662b5f691c0f9b2");

        final byte[] expected = {
                33, 0, 0, 0, 1, 123, 119, 8, -10, -84, -119, 91, 103, 72, -74, 95, -79, -125, -54, -40, -60, -58, 45, 123,
                -65, 111, 80, 84, 85, -42, 98, -75, -10, -111, -64, -7, -78, 13, 15, 32, 0, 0, 0
        };

        assertThat(byteSerializer.toBytes(optionValue), is(expected));
    }


    @Test
    void keyValueToBytes() {

        // Create key value from bytes prefixed with the key type
        final CLKeyValue clKeyValue = CLValueBuilder.key(decodeHex("012b177f0739348d33ce868b2f95bb83decf5b5dcc71279d4bec64c87f60b805d5"));

        // Assert they key type is a has
        assertThat(clKeyValue.getKeyType(), is(CLKeyInfo.KeyType.HASH_ID));

        assertThat(clKeyValue.getBytes().length, is(33));
        assertThat(clKeyValue.getBytes(), is(decodeHex("012b177f0739348d33ce868b2f95bb83decf5b5dcc71279d4bec64c87f60b805d5")));

        assertThat(clKeyValue.getKeyBytes().length, is(32));
        assertThat(clKeyValue.getKeyBytes(), is(decodeHex("2b177f0739348d33ce868b2f95bb83decf5b5dcc71279d4bec64c87f60b805d5")));

        // Assert that the key can be correctly serialized to bytes
        final byte[] expected = decodeHex("21" + // Length of key with prefix = 33 (0x21)
                                          "000000012b177f0739348d33ce868b2f95bb83decf5b5dcc71279d4bec64c87f60b805d5" +
                                          "0B"); // CL Type == 11 (0x0B)

        assertThat(byteSerializer.toBytes(clKeyValue), is(expected));
    }
}
