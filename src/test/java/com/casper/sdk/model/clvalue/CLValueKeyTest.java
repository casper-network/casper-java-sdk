package com.casper.sdk.model.clvalue;

import com.casper.sdk.model.entity.EntityAddr;
import com.casper.sdk.model.key.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

/**
 * Unit tests for the {@link CLValueKey} class
 *
 * @author ian@meywood.com
 */
class CLValueKeyTest {

    @Test
    void clValueKeyAddressableEntityKeyJsonRoundTrip() throws JsonProcessingException, JSONException {

        final String json = " {\n" +
                "        \"cl_type\": \"Key\",\n" +
                "        \"bytes\": \"11013beb396c91ff7ae62d08857cc8a787146cd4f0771b8a21d385b3f4ac6077854a\",\n" +
                "        \"parsed\": \"entity-account-3beb396c91ff7ae62d08857cc8a787146cd4f0771b8a21d385b3f4ac6077854a\"\n" +
                "      }";

        final CLValueKey clValueKey = new ObjectMapper().readValue(json, CLValueKey.class);

        assertThat(clValueKey.getValue().getTag(), is(KeyTag.ADDRESSABLE_ENTITY));
        assertThat(clValueKey.getValue(), is(instanceOf(AddressableEntityKey.class)));
        assertThat(clValueKey.getParsed(), is("entity-account-3beb396c91ff7ae62d08857cc8a787146cd4f0771b8a21d385b3f4ac6077854a"));
        assertThat(clValueKey.getBytes(), is("11013beb396c91ff7ae62d08857cc8a787146cd4f0771b8a21d385b3f4ac6077854a"));
        final AddressableEntityKey key = (AddressableEntityKey) clValueKey.getValue();
        assertThat(key.getEntityAddressTag(), is(EntityAddr.ACCOUNT));

        final String written = new ObjectMapper().writeValueAsString(clValueKey);
        JSONAssert.assertEquals(json, written, true);
    }


    @Test
    void clValueKeyByteCodeKeyEmptyJsonRoundTrip() throws JsonProcessingException {
        final String json = " {\n" +
                "        \"cl_type\": \"Key\",\n" +
                "        \"bytes\": \"12010000000000000000000000000000000000000000000000000000000000000000\",\n" +
                "        \"parsed\": \"byte-code-empty-0000000000000000000000000000000000000000000000000000000000000000\"\n" +
                "      }";

        final CLValueKey clValueKey = new ObjectMapper().readValue(json, CLValueKey.class);
        assertThat(clValueKey.getValue().getTag(), is(KeyTag.BYTE_CODE));
        assertThat(clValueKey.getValue(), is(instanceOf(ByteCodeKey.class)));
        assertThat(clValueKey.getParsed(), is("byte-code-empty-0000000000000000000000000000000000000000000000000000000000000000"));
        assertThat(clValueKey.getBytes(), is("12010000000000000000000000000000000000000000000000000000000000000000"));
        final ByteCodeKey key = (ByteCodeKey) clValueKey.getValue();
        assertThat(key.getByteCodeAddr(), is(ByteCodeAddr.EMPTY));
    }

    @Test
    void clValueKeyByteCodeKeyV1CasperWasmJsonRoundTrip() throws JsonProcessingException {
        final String json = " {\n" +
                "        \"cl_type\": \"Key\",\n" +
                "        \"bytes\": \"12003beb396c91ff7ae62d08857cc8a787146cd4f0771b8a21d385b3f4ac6077854a\",\n" +
                "        \"parsed\": \"byte-code-v1-wasm-3beb396c91ff7ae62d08857cc8a787146cd4f0771b8a21d385b3f4ac6077854a\"\n" +
                "      }";

        final CLValueKey clValueKey = new ObjectMapper().readValue(json, CLValueKey.class);
        assertThat(clValueKey.getValue().getTag(), is(KeyTag.BYTE_CODE));
        assertThat(clValueKey.getValue(), is(instanceOf(ByteCodeKey.class)));
        assertThat(clValueKey.getParsed(), is("byte-code-v1-wasm-3beb396c91ff7ae62d08857cc8a787146cd4f0771b8a21d385b3f4ac6077854a"));
        assertThat(clValueKey.getBytes(), is("12003beb396c91ff7ae62d08857cc8a787146cd4f0771b8a21d385b3f4ac6077854a"));
        final ByteCodeKey key = (ByteCodeKey) clValueKey.getValue();
        assertThat(key.getByteCodeAddr(), is(ByteCodeAddr.V1_CASPER_WASM));
    }

    @Test
    void clValueKeyBidAddrKeyUnifiedJsonRoundTrip() throws JsonProcessingException {
        final String json = " {\n" +
                "        \"cl_type\": \"Key\",\n" +
                "        \"bytes\": \"0f003beb396c91ff7ae62d08857cc8a787146cd4f0771b8a21d385b3f4ac6077854a\",\n" +
                "        \"parsed\": \"bid-addr-3beb396c91ff7ae62d08857cc8a787146cd4f0771b8a21d385b3f4ac6077854a\"\n" +
                "      }";

        final CLValueKey clValueKey = new ObjectMapper().readValue(json, CLValueKey.class);
        assertThat(clValueKey.getValue().getTag(), is(KeyTag.BID_ADDR));
        assertThat(clValueKey.getValue(), is(instanceOf(BidAddrKey.class)));
        assertThat(clValueKey.getParsed(), is("bid-addr-3beb396c91ff7ae62d08857cc8a787146cd4f0771b8a21d385b3f4ac6077854a"));
        assertThat(clValueKey.getBytes(), is("0f003beb396c91ff7ae62d08857cc8a787146cd4f0771b8a21d385b3f4ac6077854a"));
        final BidAddrKey key = (BidAddrKey) clValueKey.getValue();
        assertThat(key.getBidAddr(), is(BidAddr.UNIFIED));
    }

    @Test
    void clValueKeyBidAddrKeyValidatorJsonRoundTrip() throws JsonProcessingException {
        final String json = " {\n" +
                "        \"cl_type\": \"Key\",\n" +
                "        \"bytes\": \"0f013beb396c91ff7ae62d08857cc8a787146cd4f0771b8a21d385b3f4ac6077854a\",\n" +
                "        \"parsed\": \"bid-addr-3beb396c91ff7ae62d08857cc8a787146cd4f0771b8a21d385b3f4ac6077854a\"\n" +
                "      }";

        final CLValueKey clValueKey = new ObjectMapper().readValue(json, CLValueKey.class);
        assertThat(clValueKey.getValue().getTag(), is(KeyTag.BID_ADDR));
        assertThat(clValueKey.getValue(), is(instanceOf(BidAddrKey.class)));
        assertThat(clValueKey.getParsed(), is("bid-addr-3beb396c91ff7ae62d08857cc8a787146cd4f0771b8a21d385b3f4ac6077854a"));
        assertThat(clValueKey.getBytes(), is("0f013beb396c91ff7ae62d08857cc8a787146cd4f0771b8a21d385b3f4ac6077854a"));
        final BidAddrKey key = (BidAddrKey) clValueKey.getValue();
        assertThat(key.getBidAddr(), is(BidAddr.VALIDATOR));
    }

    @Test
    void clValueKeyBidAddrKeyDelegatorJsonRoundTrip() throws JsonProcessingException {
        final String json = " {\n" +
                "        \"cl_type\": \"Key\",\n" +
                "        \"bytes\": \"0f02022f3fb80d362ad0a922f446915a259c9aaec9ba99292b3e50ff2359c4580073099fa1fc0808d3a5b9ea9f3af4ca7c8c3655568fdf378d8afdf8a7e56e58abbfd4\",\n" +
                "        \"parsed\": \"bid-addr-022f3fb80d362ad0a922f446915a259c9aaec9ba99292b3e50ff2359c4580073099fa1fc0808d3a5b9ea9f3af4ca7c8c3655568fdf378d8afdf8a7e56e58abbfd4\"\n" +
                "      }";

        final CLValueKey clValueKey = new ObjectMapper().readValue(json, CLValueKey.class);
        assertThat(clValueKey.getValue().getTag(), is(KeyTag.BID_ADDR));
        assertThat(clValueKey.getValue(), is(instanceOf(BidAddrKey.class)));
        assertThat(clValueKey.getParsed(), is("bid-addr-022f3fb80d362ad0a922f446915a259c9aaec9ba99292b3e50ff2359c4580073099fa1fc0808d3a5b9ea9f3af4ca7c8c3655568fdf378d8afdf8a7e56e58abbfd4"));
        assertThat(clValueKey.getBytes(), is("0f02022f3fb80d362ad0a922f446915a259c9aaec9ba99292b3e50ff2359c4580073099fa1fc0808d3a5b9ea9f3af4ca7c8c3655568fdf378d8afdf8a7e56e58abbfd4"));
        final BidAddrKey key = (BidAddrKey) clValueKey.getValue();
        assertThat(key.getBidAddr(), is(BidAddr.DELEGATOR));
    }

    @Test
    void clValueKeyBidAddrKeyCreditJsonRoundTrip() throws JsonProcessingException {
        final String json = " {\n" +
                "        \"cl_type\": \"Key\",\n" +
                "        \"bytes\": \"0f0304520037cd249ccbcfeb0b9feae07d8d4f7d922cf88adc4f3e8691f9d34ccc8d097f00000000000000\",\n" +
                "        \"parsed\": \"bid-addr-04520037cd249ccbcfeb0b9feae07d8d4f7d922cf88adc4f3e8691f9d34ccc8d097f00000000000000\"\n" +
                "      }";

        final CLValueKey clValueKey = new ObjectMapper().readValue(json, CLValueKey.class);
        assertThat(clValueKey.getValue().getTag(), is(KeyTag.BID_ADDR));
        assertThat(clValueKey.getValue(), is(instanceOf(BidAddrKey.class)));
        assertThat(clValueKey.getParsed(), is("bid-addr-04520037cd249ccbcfeb0b9feae07d8d4f7d922cf88adc4f3e8691f9d34ccc8d097f00000000000000"));
        assertThat(clValueKey.getBytes(), is("0f0304520037cd249ccbcfeb0b9feae07d8d4f7d922cf88adc4f3e8691f9d34ccc8d097f00000000000000"));
        final BidAddrKey key = (BidAddrKey) clValueKey.getValue();
        assertThat(key.getBidAddr(), is(BidAddr.CREDIT));
    }

    @Test
    void messageKeyEntityContract() {
        String json = "message-entity-contract-0000000000000000000000000000000000000000000000000000000000000000-0909090909090909090909090909090909090909090909090909090909090909-1";
    }
}
