package com.casper.sdk.model.clvalue;

import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.entity.EntityAddr;
import com.casper.sdk.model.key.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syntifi.crypto.key.encdec.Hex;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.math.BigInteger;

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
    void clValueKeyMessageKeyEntityContract() throws JsonProcessingException {
        final String json = " {\n" +
                "        \"cl_type\": \"Key\",\n" +
                "        \"bytes\": \"1302000000000000000000000000000000000000000000000000000000000000000009090909090909090909090909090909090909090909090909090909090909090101000000\",\n" +
                "        \"parsed\": \"message-entity-contract-0000000000000000000000000000000000000000000000000000000000000000-0909090909090909090909090909090909090909090909090909090909090909\"\n" +
                "      }";
        final CLValueKey clValueKey = new ObjectMapper().readValue(json, CLValueKey.class);
        assertThat(clValueKey.getValue().getTag(), is(KeyTag.MESSAGE));
        assertThat(clValueKey.getValue(), is(instanceOf(MessageKey.class)));
        assertThat(clValueKey.getParsed(), is("message-entity-contract-0000000000000000000000000000000000000000000000000000000000000000-0909090909090909090909090909090909090909090909090909090909090909"));
        assertThat(clValueKey.getBytes(), is("1302000000000000000000000000000000000000000000000000000000000000000009090909090909090909090909090909090909090909090909090909090909090101000000"));
        final MessageKey key = (MessageKey) clValueKey.getValue();
        assertThat(key.getEntityAddr(), is(EntityAddr.SMART_CONTRACT));
        assertThat(key.getMessageIndex().isPresent(), is(true));
        assertThat(key.getMessageIndex().get(), is(1L));
        assertThat(key.getEntityAddrHash(), is(new Digest("0000000000000000000000000000000000000000000000000000000000000000")));
        assertThat(key.getTopicHash(), is(new Digest("0909090909090909090909090909090909090909090909090909090909090909")));
    }

    @Test
    void clValueKeyNamedKey() throws JsonProcessingException {

        final String namdedEntiryContract = "named-key-entity-contract-0101010101010101010101010101010101010101010101010101010101010101-0202020202020202020202020202020202020202020202020202020202020202";

        final String json = " {\n" +
                "        \"cl_type\": \"Key\",\n" +
                "        \"bytes\": \"140201010101010101010101010101010101010101010101010101010101010101010202020202020202020202020202020202020202020202020202020202020202\",\n" +
                "        \"parsed\": \"" + namdedEntiryContract + "\"\n" +
                "      }";


        final CLValueKey clValueKey = new ObjectMapper().readValue(json, CLValueKey.class);
        assertThat(clValueKey.getValue().getTag(), is(KeyTag.NAMED_KEY));
        assertThat(clValueKey.getValue(), is(instanceOf(NamedKeyKey.class)));
        assertThat(clValueKey.getParsed(), is("named-key-entity-contract-0101010101010101010101010101010101010101010101010101010101010101-0202020202020202020202020202020202020202020202020202020202020202"));
        assertThat(clValueKey.getBytes(), is("140201010101010101010101010101010101010101010101010101010101010101010202020202020202020202020202020202020202020202020202020202020202"));

        final NamedKeyKey key = (NamedKeyKey) clValueKey.getValue();
        assertThat(key.getBaseAddr().getEntityAddressTag(), is(EntityAddr.SMART_CONTRACT));
        assertThat(key.getBaseAddr().getKey(), is(Hex.decode("020101010101010101010101010101010101010101010101010101010101010101")));
        assertThat(key.getStringBytes(), is(Hex.decode("0202020202020202020202020202020202020202020202020202020202020202")));
    }

    @Test
    void clValueKeyBlockGlobalKeyMessageCount() throws JsonProcessingException {
        final String json = " {\n" +
                "        \"cl_type\": \"Key\",\n" +
                "        \"bytes\": \"15010000000000000000000000000000000000000000000000000000000000000000\",\n" +
                "        \"parsed\": \"block-message-count-000000000000000000000000000000000000000000000000000000000000000\"\n" +
                "      }";
        final CLValueKey clValueKey = new ObjectMapper().readValue(json, CLValueKey.class);
        assertThat(clValueKey.getValue().getTag(), is(KeyTag.BLOCK_GLOBAL));
        assertThat(clValueKey.getValue(), is(instanceOf(BlockGlobalKey.class)));
        assertThat(clValueKey.getParsed(), is("block-message-count-000000000000000000000000000000000000000000000000000000000000000"));
        assertThat(clValueKey.getBytes(), is("15010000000000000000000000000000000000000000000000000000000000000000"));
        final BlockGlobalKey key = (BlockGlobalKey) clValueKey.getValue();
        assertThat(key.getBlockGlobalAddr(), is(BlockGlobalAddr.MESSAGE_COUNT));
    }

    @Test
    void clValueKeyBlockGlobalKeyBlockTime() throws JsonProcessingException {
        final String json = " {\n" +
                "        \"cl_type\": \"Key\",\n" +
                "        \"bytes\": \"15000000000000000000000000000000000000000000000000000000000000000000\",\n" +
                "        \"parsed\": \"block-time-000000000000000000000000000000000000000000000000000000000000000\"\n" +
                "      }";
        final CLValueKey clValueKey = new ObjectMapper().readValue(json, CLValueKey.class);
        assertThat(clValueKey.getValue().getTag(), is(KeyTag.BLOCK_GLOBAL));
        assertThat(clValueKey.getValue(), is(instanceOf(BlockGlobalKey.class)));
        assertThat(clValueKey.getParsed(), is("block-time-000000000000000000000000000000000000000000000000000000000000000"));
        assertThat(clValueKey.getBytes(), is("15000000000000000000000000000000000000000000000000000000000000000000"));
        final BlockGlobalKey key = (BlockGlobalKey) clValueKey.getValue();
        assertThat(key.getBlockGlobalAddr(), is(BlockGlobalAddr.BLOCK_TIME));
    }

    @Test
    void clValueKeyBalanceHoldKeyGas() throws JsonProcessingException {
        final String json = " {\n" +
                "        \"cl_type\": \"Key\",\n" +
                "        \"bytes\": \"160001010101010101010101010101010101010101010101010101010101010101018b215c2791010000\",\n" +
                "        \"parsed\": \"balance-hold-0001010101010101010101010101010101010101010101010101010101010101018b215c2791010000\"\n" +
                "      }";
        final CLValueKey clValueKey = new ObjectMapper().readValue(json, CLValueKey.class);
        assertThat(clValueKey.getValue().getTag(), is(KeyTag.BALANCE_HOLD));
        assertThat(clValueKey.getValue(), is(instanceOf(BalanceHoldKey.class)));
        assertThat(clValueKey.getParsed(), is("balance-hold-0001010101010101010101010101010101010101010101010101010101010101018b215c2791010000"));
        assertThat(clValueKey.getBytes(), is("160001010101010101010101010101010101010101010101010101010101010101018b215c2791010000"));
        final BalanceHoldKey key = (BalanceHoldKey) clValueKey.getValue();
        assertThat(key.getBalanceHoldAddr(), is(BalanceHoldAddr.GAS));
        assertThat(key.getUrefAddr(), is(Hex.decode("0101010101010101010101010101010101010101010101010101010101010101")));
        assertThat(key.getBlockTime(), is(new BigInteger("1722942235019")));
    }

    @Test
    void clValueKeyBalanceHoldKeyProcessing() throws JsonProcessingException {
        final String json = " {\n" +
                "        \"cl_type\": \"Key\",\n" +
                "        \"bytes\": \"160101010101010101010101010101010101010101010101010101010101010101018b215c2791010000\",\n" +
                "        \"parsed\": \"balance-hold-0101010101010101010101010101010101010101010101010101010101010101018b215c2791010000\"\n" +
                "      }";
        final CLValueKey clValueKey = new ObjectMapper().readValue(json, CLValueKey.class);
        assertThat(clValueKey.getValue().getTag(), is(KeyTag.BALANCE_HOLD));
        assertThat(clValueKey.getValue(), is(instanceOf(BalanceHoldKey.class)));
        assertThat(clValueKey.getParsed(), is("balance-hold-0101010101010101010101010101010101010101010101010101010101010101018b215c2791010000"));
        assertThat(clValueKey.getBytes(), is("160101010101010101010101010101010101010101010101010101010101010101018b215c2791010000"));
        final BalanceHoldKey key = (BalanceHoldKey) clValueKey.getValue();
        assertThat(key.getBalanceHoldAddr(), is(BalanceHoldAddr.PROCESSING));
        assertThat(key.getUrefAddr(), is(Hex.decode("0101010101010101010101010101010101010101010101010101010101010101")));
        assertThat(key.getBlockTime(), is(new BigInteger("1722942235019")));
    }

    @Test
    void clValueKeyEntryPointV1AccountKey() throws JsonProcessingException {
        final String json = " {\n" +
                "        \"cl_type\": \"Key\",\n" +
                "        \"bytes\": \"17000100000000000000000000000000000000000000000000000000000000000000000202020202020202020202020202020202020202020202020202020202020202\",\n" +
                "        \"parsed\": \"entry-point-v1-0000000000000000000000000000000000000000000000000000000000000000-0202020202020202020202020202020202020202020202020202020202020202\"\n" +
                "      }";
        final CLValueKey clValueKey = new ObjectMapper().readValue(json, CLValueKey.class);
        assertThat(clValueKey.getValue().getTag(), is(KeyTag.ENTRY_POINT));
        assertThat(clValueKey.getValue(), is(instanceOf(EntryPointKey.class)));
        assertThat(clValueKey.getBytes(), is("17000100000000000000000000000000000000000000000000000000000000000000000202020202020202020202020202020202020202020202020202020202020202"));
        assertThat(clValueKey.getParsed(), is("entry-point-v2-0000000000000000000000000000000000000000000000000000000000000000-0202020202020202020202020202020202020202020202020202020202020202"));
        final EntryPointKey key = (EntryPointKey) clValueKey.getValue();
        assertThat(key.getEntryPointAddr(), is(EntryPointAddr.VM_CASPER_V1));
        assertThat(key.getEntityAddr(), is(EntityAddr.ACCOUNT));
        assertThat(key.getHashAddr(), is(Hex.decode("0000000000000000000000000000000000000000000000000000000000000000")));
        assertThat(key.getNamedBytes(), is(Hex.decode("0202020202020202020202020202020202020202020202020202020202020202")));
    }

    @Test
    void clValueKeyEntryPointV2AccountKey() throws JsonProcessingException {
        final String json = " {\n" +
                "        \"cl_type\": \"Key\",\n" +
                "        \"bytes\": \"17000100000000000000000000000000000000000000000000000000000000000000000202020202020202020202020202020202020202020202020202020202020202\",\n" +
                "        \"parsed\": \"entry-point-v2-0000000000000000000000000000000000000000000000000000000000000000-0202020202020202020202020202020202020202020202020202020202020202\"\n" +
                "      }";
        final CLValueKey clValueKey = new ObjectMapper().readValue(json, CLValueKey.class);
        assertThat(clValueKey.getValue().getTag(), is(KeyTag.ENTRY_POINT));
        assertThat(clValueKey.getValue(), is(instanceOf(EntryPointKey.class)));
        assertThat(clValueKey.getBytes(), is("17000100000000000000000000000000000000000000000000000000000000000000000202020202020202020202020202020202020202020202020202020202020202"));
        assertThat(clValueKey.getParsed(), is("entry-point-v2-0000000000000000000000000000000000000000000000000000000000000000-0202020202020202020202020202020202020202020202020202020202020202"));
        final EntryPointKey key = (EntryPointKey) clValueKey.getValue();
        assertThat(key.getEntryPointAddr(), is(EntryPointAddr.VM_CASPER_V2));
        assertThat(key.getEntityAddr(), is(EntityAddr.ACCOUNT));
        assertThat(key.getHashAddr(), is(Hex.decode("0000000000000000000000000000000000000000000000000000000000000000")));
        assertThat(key.getSelector(), is(Hex.decode("0202020202020202020202020202020202020202020202020202020202020202")));
    }
}
