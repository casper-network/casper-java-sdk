package com.casper.sdk.service.result;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

class AccountInfoTest {

    @Test
    void parseJsonAccountInfo() throws IOException {

        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        final AccountInfo accountInfo = objectMapper.readerFor(AccountInfo.class).readValue(PeerDataTest.class.getResourceAsStream("/com/casper/sdk/service/result/account-info.json"));

        assertThat(accountInfo.getApiVersion(), is("1.0.0"));
        assertThat(accountInfo.getMerkleProof(), is("01000000003b8aea2b956ea7e953dd6ab811502ec811acd2a7d6efa94bde1daa83410a033b013b8aea2b956ea7e953dd6ab811502ec811acd2a7d6efa94bde1daa83410a033b00000000c8690ed3d6b24d6377e3caeb772437a16393e77f4b4ca841c22fd155bee3af0c07010000003b8aea2b956ea7e953dd6ab811502ec811acd2a7d6efa94bde1daa83410a033b01010102000000003b140000000000e5ebd9384dc7c7f97b894573ce618a1605a1d4f3e01f55e9436f70f94827c2f20600f13d6d3376aa4828daf0d541d4f5b4709cbb9e2be72a622491e8998d7bf50a4c0f00f53876f593a36621d643694d505c71f68f7dc13e7fb3d8a9dee34683761629071a00d27b31f252648244665a20768c29b3fcdf35742444b423d5927bdff0ba898c973e005dd6fe3638f0c0348d89746c80f7b89aa5b3b2f6f5a7be05dd5bd055c53e6ec63f000183025f9ac0aa0bcd5118eb1c7aa732ccdb6daa8e3c0b9f440e530e82bd68f75200abebba009b2ed4b38e582ccaf38adc3dad9130521580d67152aa3f2a55f70ab861007fd7411da0b466de3671341f285b5af5d2ab4b8d962a81f115c322618619d95e6301845ac3820934247a8c4261f26f9c4910f33a10f889ce2484d2d9cd790a87375364004809138a19df8ea2e507fb8c502b97baddac4e23983bb6f5f72230c978b1d4067000861e54bf9d6e784b66e459af1a1e30193d4aac0cf125b5dfe177bb60f1dfd27b86004a734a382bb14d796fa372709a107cdb48fa768a4f3c29cf89921da489080e508700cd988bd17e6da9498522205c1250466c3450e1dc14af69d5ff06873006fafe42890056d46d4e354c3ecacd9c902e7befe9b00a597256fa8e34391925ee283109dbde8a00fd92e9a5dd412628d2bd482c3d1edfd4275a1a37006f1d88c0ec1a01f5746faba500a1f800519032f82630a58ae78a79c15fbd8a09f3f528ab3120992412eb68a9d4d0003f377ce8405ed69515f77c096cf8fe841c8662f0ae349e443489b84fcde0da64d4007f0263ef9108fbf84bb29fd01de138acbd7cc9124363da7847d2607068a4f3e0e10023eeb5c27e754d6feedce91b932cca1e6d5679e3833e2140e90c4b7401f2bf59ec00d1429c035c03ea6f929f6ad283bb7ec5f8ebca4c4d50334bd4585d36a1d548320000090000000101bf93aa0492deea75599c6009578b30a0efc191e7b3e452719d9f3e61b70b3d9e0201bef0f909db1ae34cf06df588d6f8e2a346a471d6ef857f521dc548c1e23d8f4b0301a199ebff2431380a0f0f0c9f5ee36047edd02ba7af4131d0c8dc59e84a2703340401c09841afbcb0027592437f82da76c43e1afd95a4cbf272385de972a4e636a09b0501be1aadfd431e7e80f18cbed6e1a3a7c3c223df15150419339ea564345fdb96b00601d929afab5dfb5c48f4b54f2c3c03010d50f214be7605698c7b8fa5683184b88307019d021217fcda2a72bf302d71ae8e4a2eadfa1eae3b33be50c5df01cb174db1ec0a003ee33a015624e34139fb293461fe1aefee3d31ed69dad5b1d38c60d0ce9e05a20c0062c44833531e85e3e942c7e36ae958f1cb927a1652b80f4e56e9763aa2dbb7b4"));

        final StoredValue storedValue = accountInfo.getStoredValue();
        assertThat(storedValue, is(notNullValue()));

        final Account account = storedValue.getAccount();
        assertThat(account, is(notNullValue()));
        assertThat(account.getAccountHash(), is("account-hash-3b8aea2b956ea7e953dd6ab811502ec811acd2a7d6efa94bde1daa83410a033b"));
        assertThat(account.getMainPurse(), is("uref-c8690ed3d6b24d6377e3caeb772437a16393e77f4b4ca841c22fd155bee3af0c-007"));

        ActionThresholds actionThresholds = account.getActionThresholds();
        assertThat(actionThresholds.getDeployment(), is(1));
        assertThat(actionThresholds.getKeyManagement(), is(1));

        final List<AssociatedKey> associatedKeys = account.getAssociatedKeys();
        assertThat(associatedKeys, is(notNullValue()));

        final AssociatedKey associatedKey = associatedKeys.get(0);
        assertThat(associatedKey.getAccountHash(), is("account-hash-3b8aea2b956ea7e953dd6ab811502ec811acd2a7d6efa94bde1daa83410a033b"));
        assertThat(associatedKey.getWeight(), is(1));


    }
}