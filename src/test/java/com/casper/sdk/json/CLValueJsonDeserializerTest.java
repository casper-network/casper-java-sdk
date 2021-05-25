package com.casper.sdk.json;

import com.casper.sdk.domain.CLType;
import com.casper.sdk.domain.CLValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigInteger;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Unit tests for the {@link CLValueJsonDeserializer}
 */
class CLValueJsonDeserializerTest {

    private static final String U512_JSON = """
            {
               "cl_type": "U512",
               "bytes": "05005550b405",
               "parsed": "24500000000"
            }""";

    private static final String U64_JSON = """
            {
              "cl_type": "U64",
              "bytes": "01e703000000000000",
              "parsed": 999
            }""";

    private static final String BYTE_ARRAY_32_JSON = """
            {
              "cl_type": {
                "ByteArray": 32
              },
              "bytes": "0101010101010101010101010101010101010101010101010101010101010101",
              "parsed": "0101010101010101010101010101010101010101010101010101010101010101"
            }""";

    @Test
    void deserializeU512CLValue() throws IOException {

        final CLValue clValue = new ObjectMapper().reader().readValue(U512_JSON, CLValue.class);
        assertThat(clValue, is(notNullValue()));
        assertThat(clValue.getCLTypeInfo().getType(), is(CLType.U512));
        assertThat(clValue.getBytes(), is(CLValue.fromString("05005550b405")));
        assertThat(clValue.getParsed(), is("24500000000"));
    }

    @Test
    void deserializeByteArray32CLValue() throws IOException {
        final CLValue clValue = new ObjectMapper().reader().readValue(BYTE_ARRAY_32_JSON, CLValue.class);
        assertThat(clValue, is(notNullValue()));
        assertThat(clValue.getCLTypeInfo().getType(), is(CLType.BYTE_ARRAY));
        assertThat(clValue.getBytes(), is(CLValue.fromString("0101010101010101010101010101010101010101010101010101010101010101")));
        assertThat(clValue.getParsed(), is("0101010101010101010101010101010101010101010101010101010101010101"));
    }

    @Test
    void deserializeU64CLValue() throws IOException {
        final CLValue clValue = new ObjectMapper().reader().readValue(U64_JSON, CLValue.class);
        assertThat(clValue, is(notNullValue()));
        assertThat(clValue.getCLTypeInfo().getType(), is(CLType.U64));
        assertThat(clValue.getBytes(), is(CLValue.fromString("01e703000000000000")));
        assertThat(clValue.getParsed(), is(BigInteger.valueOf(999L)));
    }
}