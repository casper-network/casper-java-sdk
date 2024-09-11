package com.casper.sdk.model.key;

import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.entity.EntityAddr;
import com.casper.sdk.model.uref.URef;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syntifi.crypto.key.encdec.Hex;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * Unit tests for keys
 *
 * @author ian@meywood.com
 */
class KeyTest {

    @Test
    void accountKeyFromKeyString() throws Exception {

        final String strKey = "account-hash-0101010101010101010101010101010101010101010101010101010101010101";
        final Key key = fromJson(strKey);
        assertThat(key, is(notNullValue(Key.class)));
        assertThat(key.getTag(), is(KeyTag.ACCOUNT));
        assertThat(key.getKey(), is(Hex.decode("0101010101010101010101010101010101010101010101010101010101010101")));
        assertThat(key.toString(), is(strKey));
        assertThat(key.getAlgoTaggedHex(), is("000101010101010101010101010101010101010101010101010101010101010101"));
    }

    @Test
    void hashKeyFromKeyString() throws Exception {

        final String strKey = "hash-0202020202020202020202020202020202020202020202020202020202020202";
        final Key key = fromJson(strKey);
        assertThat(key, is(notNullValue(Key.class)));
        assertThat(key.getTag(), is(KeyTag.HASH));
        assertThat(key.getKey(), is(Hex.decode("0202020202020202020202020202020202020202020202020202020202020202")));
        assertThat(key.toString(), is(strKey));
        assertThat(key.getAlgoTaggedHex(), is("010202020202020202020202020202020202020202020202020202020202020202"));
    }

    @Test
    void urefKeyFromKeyString() throws Exception {

        final String strKey = "uref-0303030303030303030303030303030303030303030303030303030303030303-001";
        final Key key = fromJson(strKey);
        assertThat(key, is(instanceOf(URefKey.class)));
        assertThat(key.getTag(), is(KeyTag.UREF));
        assertThat(key.getKey(), is(Hex.decode("030303030303030303030303030303030303030303030303030303030303030301")));
        assertThat(key.toString(), is(strKey));
        assertThat(key.getAlgoTaggedHex(), is("02030303030303030303030303030303030303030303030303030303030303030301"));
        assertThat(((URefKey) key).getURef(), is(URef.fromString("uref-0303030303030303030303030303030303030303030303030303030303030303-001")));

        final String hexKey = "02030303030303030303030303030303030303030303030303030303030303030301";
        final Key fromTaggedHexString = Key.fromTaggedHexString(hexKey);
        assertThat(key, is(instanceOf(URefKey.class)));
        assertThat(((URefKey) key).getURef(), is(((URefKey) fromTaggedHexString).getURef()));
    }

    @Test
    void transferKeyFromKeyString() throws Exception {

        final String strKey = "transfer-0404040404040404040404040404040404040404040404040404040404040404";
        final Key key = fromJson(strKey);
        assertThat(key, is(notNullValue(Key.class)));
        assertThat(key.getTag(), is(KeyTag.TRANSFER));
        assertThat(key.getKey(), is(Hex.decode("0404040404040404040404040404040404040404040404040404040404040404")));
        assertThat(key.toString(), is(strKey));
        assertThat(key.getAlgoTaggedHex(), is("030404040404040404040404040404040404040404040404040404040404040404"));
    }

    @Test
    void deployKeyFromKeyString() throws Exception {

        final String strKey = "deploy-0505050505050505050505050505050505050505050505050505050505050505";
        final Key key = fromJson(strKey);
        assertThat(key, is(notNullValue(Key.class)));
        assertThat(key.getTag(), is(KeyTag.DEPLOY_INFO));
        assertThat(key.getKey(), is(Hex.decode("0505050505050505050505050505050505050505050505050505050505050505")));
        assertThat(key.toString(), is(strKey));
        assertThat(key.getAlgoTaggedHex(), is("040505050505050505050505050505050505050505050505050505050505050505"));
    }

    @Test
    void eraInfoKeyFromKeyString() throws Exception {

        final String strKey = "era-12345";
        final Key key = fromJson(strKey);
        assertThat(key, is(instanceOf(EraInfoKey.class)));
        assertThat(key.getTag(), is(KeyTag.ERA_INFO));
        assertThat(key.getKey(), is(Hex.decode("3930000000000000")));
        assertThat(key.toString(), is(strKey));
        assertThat(key.getAlgoTaggedHex(), is("053930000000000000"));
    }

    @Test
    void balanceKeyFromKeyString() throws Exception {

        final String strKey = "balance-0707070707070707070707070707070707070707070707070707070707070707";
        final Key key = fromJson(strKey);
        assertThat(key, is(notNullValue(Key.class)));
        assertThat(key.getTag(), is(KeyTag.BALANCE));
        assertThat(key.getKey(), is(Hex.decode("0707070707070707070707070707070707070707070707070707070707070707")));
        assertThat(key.toString(), is(strKey));
        assertThat(key.getAlgoTaggedHex(), is("060707070707070707070707070707070707070707070707070707070707070707"));
    }

    @Test
    void bidKeyFromKeyString() throws Exception {

        final String strKey = "bid-0808080808080808080808080808080808080808080808080808080808080808";
        final Key key = fromJson(strKey);
        assertThat(key, is(notNullValue(Key.class)));
        assertThat(key.getTag(), is(KeyTag.BID));
        assertThat(key.getKey(), is(Hex.decode("0808080808080808080808080808080808080808080808080808080808080808")));
        assertThat(key.toString(), is(strKey));
        assertThat(key.getAlgoTaggedHex(), is("070808080808080808080808080808080808080808080808080808080808080808"));
    }

    @Test
    void withdrawKeyFromKeyString() throws Exception {

        final String strKey = "withdraw-0909090909090909090909090909090909090909090909090909090909090909";
        final Key key = fromJson(strKey);
        assertThat(key, is(notNullValue(Key.class)));
        assertThat(key.getTag(), is(KeyTag.WITHDRAW));
        assertThat(key.getKey(), is(Hex.decode("0909090909090909090909090909090909090909090909090909090909090909")));
        assertThat(key.toString(), is(strKey));
        assertThat(key.getAlgoTaggedHex(), is("080909090909090909090909090909090909090909090909090909090909090909"));
    }

    @Test
    void dictionaryKeyFromKeyString() throws Exception {

        final String strKey = "dictionary-1010101010101010101010101010101010101010101010101010101010101010";
        final Key key = fromJson(strKey);
        assertThat(key, is(notNullValue(Key.class)));
        assertThat(key.getTag(), is(KeyTag.DICTIONARY));
        assertThat(key.getKey(), is(Hex.decode("1010101010101010101010101010101010101010101010101010101010101010")));
        assertThat(key.toString(), is(strKey));
        assertThat(key.getAlgoTaggedHex(), is("091010101010101010101010101010101010101010101010101010101010101010"));
    }

    @Test
    void systemContractRegistryKeyFromKeyString() throws Exception {

        final String strKey = "system-contract-registry-0909090909090909090909090909090909090909090909090909090909090909";
        final Key key = fromJson(strKey);
        assertThat(key, is(notNullValue(Key.class)));
        assertThat(key.getTag(), is(KeyTag.SYSTEM_ENTITY_REGISTRY));
        assertThat(key.getKey(), is(Hex.decode("0909090909090909090909090909090909090909090909090909090909090909")));
        assertThat(key.toString(), is(strKey));
        assertThat(key.getAlgoTaggedHex(), is("0a0909090909090909090909090909090909090909090909090909090909090909"));
    }

    @Test
    void eraSummaryKeyFromKeyString() throws Exception {

        final String strKey = "era-summary-0909090909090909090909090909090909090909090909090909090909090909";
        final Key key = fromJson(strKey);
        assertThat(key, is(notNullValue(Key.class)));
        assertThat(key.getTag(), is(KeyTag.ERA_SUMMARY));
        assertThat(key.getKey(), is(Hex.decode("0909090909090909090909090909090909090909090909090909090909090909")));
        assertThat(key.toString(), is(strKey));
        assertThat(key.getAlgoTaggedHex(), is("0b0909090909090909090909090909090909090909090909090909090909090909"));
    }

    @Test
    void unbondKeyFromKeyString() throws Exception {

        final String strKey = "unbond-0909090909090909090909090909090909090909090909090909090909090909";
        final Key key = fromJson(strKey);
        assertThat(key, is(notNullValue(Key.class)));
        assertThat(key.getTag(), is(KeyTag.UNBOND));
        assertThat(key.getKey(), is(Hex.decode("0909090909090909090909090909090909090909090909090909090909090909")));
        assertThat(key.toString(), is(strKey));
        assertThat(key.getAlgoTaggedHex(), is("0c0909090909090909090909090909090909090909090909090909090909090909"));
    }

    @Test
    void chainspecRegistryKeyFromKeyString() throws Exception {

        final String strKey = "chainspec-registry-0909090909090909090909090909090909090909090909090909090909090909";
        final Key key = fromJson(strKey);
        assertThat(key, is(notNullValue(Key.class)));
        assertThat(key.getTag(), is(KeyTag.CHAINSPEC_REGISTRY));
        assertThat(key.getKey(), is(Hex.decode("0909090909090909090909090909090909090909090909090909090909090909")));
        assertThat(key.toString(), is(strKey));
        assertThat(key.getAlgoTaggedHex(), is("0d0909090909090909090909090909090909090909090909090909090909090909"));
    }

    @Test
    void checksumRegistryKeyFromKeyString() throws Exception {

        final String strKey = "checksum-registry-0909090909090909090909090909090909090909090909090909090909090909";
        final Key key = fromJson(strKey);
        assertThat(key, is(notNullValue(Key.class)));
        assertThat(key.getTag(), is(KeyTag.CHECKSUM_REGISTRY));
        assertThat(key.getKey(), is(Hex.decode("0909090909090909090909090909090909090909090909090909090909090909")));
        assertThat(key.toString(), is(strKey));
        assertThat(key.getAlgoTaggedHex(), is("0e0909090909090909090909090909090909090909090909090909090909090909"));
    }

    @Test
    void contractPackageKeyFromKeyString() throws Exception {

        final String strKey = "contract-package-0909090909090909090909090909090909090909090909090909090909090909";
        final Key key = fromJson(strKey);
        assertThat(key, is(notNullValue(Key.class)));
        assertThat(key.getTag(), is(KeyTag.HASH));
        assertThat(key.getKey(), is(Hex.decode("0909090909090909090909090909090909090909090909090909090909090909")));
        assertThat(key.toString(), is("hash-0909090909090909090909090909090909090909090909090909090909090909"));
        assertThat(key.getAlgoTaggedHex(), is("010909090909090909090909090909090909090909090909090909090909090909"));
    }

    @Test
    void contractWasmKeyFromKeyString() throws Exception {

        final String strKey = "contract-wasm-0909090909090909090909090909090909090909090909090909090909090909";
        final Key key = fromJson(strKey);
        assertThat(key, is(notNullValue(Key.class)));
        assertThat(key.getTag(), is(KeyTag.HASH));
        assertThat(key.getKey(), is(Hex.decode("0909090909090909090909090909090909090909090909090909090909090909")));
        assertThat(key.toString(), is("hash-0909090909090909090909090909090909090909090909090909090909090909"));
        assertThat(key.getAlgoTaggedHex(), is("010909090909090909090909090909090909090909090909090909090909090909"));
    }

    @Test
    void contractKeyFromKeyString() throws Exception {

        final String strKey = "contract-0909090909090909090909090909090909090909090909090909090909090909";
        final Key key = fromJson(strKey);
        assertThat(key, is(notNullValue(Key.class)));
        assertThat(key.getTag(), is(KeyTag.HASH));
        assertThat(key.getKey(), is(Hex.decode("0909090909090909090909090909090909090909090909090909090909090909")));
        assertThat(key.toString(), is("hash-0909090909090909090909090909090909090909090909090909090909090909"));
        assertThat(key.getAlgoTaggedHex(), is("010909090909090909090909090909090909090909090909090909090909090909"));
    }

    @Test
    void bidAddrKeyUnifiedFromKeyString() throws Exception {

        final String strKey = "bid-addr-002f3fb80d362ad0a922f446915a259c9aaec9ba99292b3e50ff2359c458007309";
        final Key key = fromJson(strKey);
        assertThat(key, is(instanceOf(BidAddrKey.class)));
        assertThat(key.getTag(), is(KeyTag.BID_ADDR));
        assertThat(key.getKey(), is(Hex.decode("002f3fb80d362ad0a922f446915a259c9aaec9ba99292b3e50ff2359c458007309")));
        assertThat(key.toString(), is(strKey));
        assertThat(key.getAlgoTaggedHex(), is("0f002f3fb80d362ad0a922f446915a259c9aaec9ba99292b3e50ff2359c458007309"));
        assertThat(((BidAddrKey) key).getBidAddr(), is(BidAddr.UNIFIED));
    }

    @Test
    void bidAddrKeyValidatorFromKeyString() throws Exception {

        final String strKey = "bid-addr-012f3fb80d362ad0a922f446915a259c9aaec9ba99292b3e50ff2359c458007309";
        final Key key = fromJson(strKey);
        assertThat(key, is(instanceOf(BidAddrKey.class)));
        assertThat(key.getTag(), is(KeyTag.BID_ADDR));
        assertThat(key.getKey(), is(Hex.decode("012f3fb80d362ad0a922f446915a259c9aaec9ba99292b3e50ff2359c458007309")));
        assertThat(key.toString(), is(strKey));
        assertThat(key.getAlgoTaggedHex(), is("0f012f3fb80d362ad0a922f446915a259c9aaec9ba99292b3e50ff2359c458007309"));
        assertThat(((BidAddrKey) key).getBidAddr(), is(BidAddr.VALIDATOR));
    }

    @Test
    void bidAddrKeyDelegatorFromKeyString() throws Exception {

        final String strKey = "bid-addr-022f3fb80d362ad0a922f446915a259c9aaec9ba99292b3e50ff2359c4580073099fa1fc0808d3a5b9ea9f3af4ca7c8c3655568fdf378d8afdf8a7e56e58abbfd4";
        final Key key = fromJson(strKey);
        assertThat(key, is(instanceOf(BidAddrKey.class)));
        assertThat(key.getTag(), is(KeyTag.BID_ADDR));
        assertThat(key.getKey(), is(Hex.decode("022f3fb80d362ad0a922f446915a259c9aaec9ba99292b3e50ff2359c4580073099fa1fc0808d3a5b9ea9f3af4ca7c8c3655568fdf378d8afdf8a7e56e58abbfd4")));
        assertThat(key.toString(), is(strKey));
        assertThat(key.getAlgoTaggedHex(), is("0f022f3fb80d362ad0a922f446915a259c9aaec9ba99292b3e50ff2359c4580073099fa1fc0808d3a5b9ea9f3af4ca7c8c3655568fdf378d8afdf8a7e56e58abbfd4"));
        assertThat(((BidAddrKey) key).getBidAddr(), is(BidAddr.DELEGATOR));
    }

    @Test
    void bidAddrKeyCreditFromKeyString() throws Exception {

        final String strKey = "bid-addr-04520037cd249ccbcfeb0b9feae07d8d4f7d922cf88adc4f3e8691f9d34ccc8d097f00000000000000";
        final Key key = fromJson(strKey);
        assertThat(key, is(instanceOf(BidAddrKey.class)));
        assertThat(key.getTag(), is(KeyTag.BID_ADDR));
        assertThat(key.getKey(), is(Hex.decode("04520037cd249ccbcfeb0b9feae07d8d4f7d922cf88adc4f3e8691f9d34ccc8d097f00000000000000")));
        assertThat(key.toString(), is(strKey));
        assertThat(key.getAlgoTaggedHex(), is("0f04520037cd249ccbcfeb0b9feae07d8d4f7d922cf88adc4f3e8691f9d34ccc8d097f00000000000000"));
        assertThat(((BidAddrKey) key).getBidAddr(), is(BidAddr.CREDIT));
    }

    @Test
    void packageKeyFromKeyString() throws Exception {

        final String strKey = "package-0909090909090909090909090909090909090909090909090909090909090909";
        final Key key = fromJson(strKey);
        assertThat(key, is(instanceOf(Key.class)));
        assertThat(key.getTag(), is(KeyTag.PACKAGE));
        assertThat(key.getKey(), is(Hex.decode("0909090909090909090909090909090909090909090909090909090909090909")));
        assertThat(key.toString(), is(strKey));
        assertThat(key.getAlgoTaggedHex(), is("100909090909090909090909090909090909090909090909090909090909090909"));
    }

    @Test
    void addressableEntitySystemKeyFromKeyString() throws Exception {

        final String strKey = "entity-system-0101010101010101010101010101010101010101010101010101010101010101";
        final Key key = fromJson(strKey);
        assertThat(key, is(instanceOf(AddressableEntityKey.class)));
        assertThat(key.getTag(), is(KeyTag.ADDRESSABLE_ENTITY));
        assertThat(key.getKey(), is(Hex.decode("000101010101010101010101010101010101010101010101010101010101010101")));
        assertThat(key.toString(), is(strKey));
        assertThat(key.getAlgoTaggedHex(), is("11000101010101010101010101010101010101010101010101010101010101010101"));
        assertThat(((AddressableEntityKey) key).getEntityAddressTag(), is(EntityAddr.SYSTEM));
    }

    @Test
    void addressableEntityAccountKeyFromKeyString() throws Exception {

        final String strKey = "entity-account-0101010101010101010101010101010101010101010101010101010101010101";
        final Key key = fromJson(strKey);
        assertThat(key, is(instanceOf(AddressableEntityKey.class)));
        assertThat(key.getTag(), is(KeyTag.ADDRESSABLE_ENTITY));
        assertThat(key.getKey(), is(Hex.decode("010101010101010101010101010101010101010101010101010101010101010101")));
        assertThat(key.toString(), is(strKey));
        assertThat(key.getAlgoTaggedHex(), is("11010101010101010101010101010101010101010101010101010101010101010101"));
        assertThat(((AddressableEntityKey) key).getEntityAddressTag(), is(EntityAddr.ACCOUNT));
    }

    @Test
    void addressableEntityContractKeyFromKeyString() throws Exception {

        final String strKey = "entity-contract-0101010101010101010101010101010101010101010101010101010101010101";
        final Key key = fromJson(strKey);
        assertThat(key, is(instanceOf(AddressableEntityKey.class)));
        assertThat(key.getTag(), is(KeyTag.ADDRESSABLE_ENTITY));
        assertThat(key.getKey(), is(Hex.decode("020101010101010101010101010101010101010101010101010101010101010101")));
        assertThat(key.toString(), is(strKey));
        assertThat(key.getAlgoTaggedHex(), is("11020101010101010101010101010101010101010101010101010101010101010101"));
        assertThat(((AddressableEntityKey) key).getEntityAddressTag(), is(EntityAddr.SMART_CONTRACT));
    }

    @Test
    void byteCodeEmptyKeyFromKeyString() throws Exception {

        final String strKey = "byte-code-empty-0101010101010101010101010101010101010101010101010101010101010101";
        final Key key = fromJson(strKey);
        assertThat(key, is(instanceOf(ByteCodeKey.class)));
        assertThat(key.getTag(), is(KeyTag.BYTE_CODE));
        assertThat(key.getKey(), is(Hex.decode("010101010101010101010101010101010101010101010101010101010101010101")));
        assertThat(key.toString(), is(strKey));
        assertThat(key.getAlgoTaggedHex(), is("12010101010101010101010101010101010101010101010101010101010101010101"));
        assertThat(((ByteCodeKey) key).getByteCodeAddr(), is(ByteCodeAddr.EMPTY));
    }

    @Test
    void byteCodeV1WasmKeyFromKeyString() throws Exception {

        final String strKey = "byte-code-v1-wasm-0101010101010101010101010101010101010101010101010101010101010101";
        final Key key = fromJson(strKey);
        assertThat(key, is(instanceOf(ByteCodeKey.class)));
        assertThat(key.getTag(), is(KeyTag.BYTE_CODE));
        assertThat(key.getKey(), is(Hex.decode("000101010101010101010101010101010101010101010101010101010101010101")));
        assertThat(key.toString(), is(strKey));
        assertThat(key.getAlgoTaggedHex(), is("12000101010101010101010101010101010101010101010101010101010101010101"));
        assertThat(((ByteCodeKey) key).getByteCodeAddr(), is(ByteCodeAddr.V1_CASPER_WASM));
    }

    @Test
    void messageTopicKeyFromKeyString() throws Exception {

        final String strKey = "message-topic-entity-contract-55d4a6915291da12afded37fa5bc01f0803a2f0faf6acb7ec4c7ca6ab76f3330-5721a6d9d7a9afe5dfdb35276fb823bed0f825350e4d865a5ec0110c380de4e1";
        final Key key = fromJson(strKey);
        assertThat(key, is(instanceOf(MessageKey.class)));
        assertThat(key.getTag(), is(KeyTag.MESSAGE));
        assertThat(key.getKey(), is(Hex.decode("0255d4a6915291da12afded37fa5bc01f0803a2f0faf6acb7ec4c7ca6ab76f33305721a6d9d7a9afe5dfdb35276fb823bed0f825350e4d865a5ec0110c380de4e100")));
        assertThat(key.toString(), is(strKey));
        assertThat(key.getAlgoTaggedHex(), is("130255d4a6915291da12afded37fa5bc01f0803a2f0faf6acb7ec4c7ca6ab76f33305721a6d9d7a9afe5dfdb35276fb823bed0f825350e4d865a5ec0110c380de4e100"));
        assertThat(((MessageKey) key).getEntityAddrHash(), is(new Digest("55d4a6915291da12afded37fa5bc01f0803a2f0faf6acb7ec4c7ca6ab76f3330")));
        assertThat(((MessageKey) key).getTopicHash(), is(new Digest("5721a6d9d7a9afe5dfdb35276fb823bed0f825350e4d865a5ec0110c380de4e1")));
        assertThat(((MessageKey) key).getMessageIndex().isPresent(), is(false));
    }

    @Test
    void messageIndexKeyFromKeyString() throws Exception {

        final String strKey = "message-entity-contract-55d4a6915291da12afded37fa5bc01f0803a2f0faf6acb7ec4c7ca6ab76f3330-5721a6d9d7a9afe5dfdb35276fb823bed0f825350e4d865a5ec0110c380de4e1-f";
        final Key key = fromJson(strKey);
        assertThat(key, is(instanceOf(MessageKey.class)));
        assertThat(key.getTag(), is(KeyTag.MESSAGE));
        assertThat(key.toString(), is(strKey));
        assertThat(key.getAlgoTaggedHex(), is("130255d4a6915291da12afded37fa5bc01f0803a2f0faf6acb7ec4c7ca6ab76f33305721a6d9d7a9afe5dfdb35276fb823bed0f825350e4d865a5ec0110c380de4e1010f000000"));
        assertThat(((MessageKey) key).getEntityAddrHash(), is(new Digest("55d4a6915291da12afded37fa5bc01f0803a2f0faf6acb7ec4c7ca6ab76f3330")));
        assertThat(((MessageKey) key).getTopicHash(), is(new Digest("5721a6d9d7a9afe5dfdb35276fb823bed0f825350e4d865a5ec0110c380de4e1")));
        assertThat(((MessageKey) key).getMessageIndex().isPresent(), is(true));
        assertThat(((MessageKey) key).getMessageIndex().get(), is(15L));
        assertThat(key.getKey(), is(Hex.decode("0255d4a6915291da12afded37fa5bc01f0803a2f0faf6acb7ec4c7ca6ab76f33305721a6d9d7a9afe5dfdb35276fb823bed0f825350e4d865a5ec0110c380de4e1010f000000")));
    }

    @Test
    void namedKeyFromKeyString() throws Exception {

        final String strKey = "named-key-entity-system-0101010101010101010101010101010101010101010101010101010101010101-0202020202020202020202020202020202020202020202020202020202020202";
        final Key key = fromJson(strKey);
        assertThat(key, is(instanceOf(NamedKeyKey.class)));
        assertThat(key.getTag(), is(KeyTag.NAMED_KEY));
        assertThat(key.toString(), is(strKey));
        assertThat(key.getAlgoTaggedHex(), is("14000001010101010101010101010101010101010101010101010101010101010101010202020202020202020202020202020202020202020202020202020202020202"));
        assertThat(((NamedKeyKey) key).getBaseAddr().getKey(), is(Hex.decode("000101010101010101010101010101010101010101010101010101010101010101")));
        assertThat(((NamedKeyKey) key).getStringBytes(), is(Hex.decode("0202020202020202020202020202020202020202020202020202020202020202")));
        assertThat(key.getKey(), is(Hex.decode("000001010101010101010101010101010101010101010101010101010101010101010202020202020202020202020202020202020202020202020202020202020202")));
    }

    @Test
    void blockTimeKeyFromKeyString() throws Exception {

        final String strKey = "block-time-0909090909090909090909090909090909090909090909090909090909090909";
        final Key key = fromJson(strKey);
        assertThat(key, is(instanceOf(BlockGlobalKey.class)));
        assertThat(key.getTag(), is(KeyTag.BLOCK_GLOBAL));
        assertThat(key.toString(), is("block-time-0909090909090909090909090909090909090909090909090909090909090909"));
        assertThat(key.getAlgoTaggedHex(), is("15000909090909090909090909090909090909090909090909090909090909090909"));
        assertThat(((BlockGlobalKey) key).getBlockGlobalAddr(), is(BlockGlobalAddr.BLOCK_TIME));
        assertThat(key.getKey(), is(Hex.decode("000909090909090909090909090909090909090909090909090909090909090909")));
    }

    @Test
    void blockMessageCountKeyFromKeyString() throws Exception {

        final String strKey = "block-message-count-0909090909090909090909090909090909090909090909090909090909090909";
        final Key key = fromJson(strKey);
        assertThat(key, is(instanceOf(BlockGlobalKey.class)));
        assertThat(key.getTag(), is(KeyTag.BLOCK_GLOBAL));
        assertThat(key.toString(), is(strKey));
        assertThat(key.getAlgoTaggedHex(), is("15010909090909090909090909090909090909090909090909090909090909090909"));
        assertThat(((BlockGlobalKey) key).getBlockGlobalAddr(), is(BlockGlobalAddr.MESSAGE_COUNT));
        assertThat(key.getKey(), is(Hex.decode("010909090909090909090909090909090909090909090909090909090909090909")));
    }

    @Test
    void entryPointKeyFromKeyString() throws Exception {

        final String strKey = "entry-point-v2-entity-contract-2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a-1";
        final Key key = fromJson(strKey);
        assertThat(key, is(instanceOf(EntryPointKey.class)));
        assertThat(key.getTag(), is(KeyTag.ENTRY_POINT));
        assertThat(key.toString(), is(strKey));
        assertThat(key.getAlgoTaggedHex(), is("1701022a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a01000000"));
        assertThat(((EntryPointKey) key).getEntryPointAddr(), is(EntryPointAddr.VM_CASPER_V2));
        assertThat(((EntryPointKey) key).getHashAddr(), is(Hex.decode("2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a")));
        assertThat(((EntryPointKey) key).getSelector(), is(1L));
        assertThat(key.getKey(), is(Hex.decode("01022a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a01000000")));
    }

    @Test
    void balanceHoldKeyFromKeyString() throws Exception {

        final String strKey = "balance-hold-0000000000000000000000000000000000000000000000000000000000000000000000000000000000";
        final Key key = fromJson(strKey);
        assertThat(key, is(instanceOf(BalanceHoldKey.class)));
        assertThat(key.getTag(), is(KeyTag.BALANCE_HOLD));
        assertThat(key.toString(), is(strKey));
        assertThat(key.getAlgoTaggedHex(), is("160000000000000000000000000000000000000000000000000000000000000000000000000000000000"));
        assertThat(((BalanceHoldKey) key).getBalanceHoldAddr(), is(BalanceHoldAddr.GAS));
        assertThat(key.getKey(), is(Hex.decode("0000000000000000000000000000000000000000000000000000000000000000000000000000000000")));
    }

    @Test
    void nullKeyFromJson() throws Exception {
        final Key key = fromJson(null);
        assertThat(key, is(nullValue()));
    }

    private Key fromJson(final String strKey) throws JsonProcessingException {
        //noinspection VulnerableCodeUsages
        return new ObjectMapper().readValue(strKey != null ? "\"" + strKey + "\"" : "null", Key.class);
    }
}

