package com.casper.sdk.json;

import com.casper.sdk.domain.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Tests that a {@link Deploy} can be parsed from JSON
 */
public class DeployJsonDeserializerTest {

    private static final String DEPLOY_TRANSFER_JSON = "/com/casper/sdk/json/deploy-transfer.json";

    private Deploy deploy;

    @BeforeEach
    void setUp() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final InputStream in = getClass().getResourceAsStream(DEPLOY_TRANSFER_JSON);
        deploy = mapper.reader().readValue(in, Deploy.class);
    }

    @Test
    public void testDeployHashFromJson() {
        assertThat(deploy.getHash(), is(notNullValue(Digest.class)));
        assertThat(deploy.getHash(), is(new Digest("d7a68bbe656a883d04bba9f26aa340dbe3f8ec99b2adb63b628f2bc920431998")));
    }

    @Test
    public void testDeployDeployHeaderFromJson() {
        assertThat(deploy.getHeader(), is(notNullValue(DeployHeader.class)));

        assertThat(deploy.getHeader().getAccount().toHex(), is("017f747b67bd3fe63c2a736739dfe40156d622347346e70f68f51c178a75ce5537"));
        assertThat(deploy.getHeader().getTimestamp(), is("2021-05-04T14:20:35.104Z"));
        assertThat(deploy.getHeader().getBodyHash().getHash(), is("f2e0782bba4a0a9663cafc7d707fd4a74421bc5bfef4e368b7e8f38dfab87db8"));
        assertThat(deploy.getHeader().getGasPrice(), is(2));
        assertThat(deploy.getHeader().getDependencies().size(), is(2));
        assertThat(deploy.getHeader().getDependencies().get(0).getHash(), is("0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f"));
        assertThat(deploy.getHeader().getDependencies().get(1).getHash(), is("1010101010101010101010101010101010101010101010101010101010101010"));
        assertThat(deploy.getHeader().getChainName(), is("mainnet"));
    }

    @Test
    void testDeployPaymentFromJson() {
        assertThat(deploy.getPayment(), is(instanceOf(Payment.class)));
        Payment payment = deploy.getPayment();
        assertThat(payment.getModuleBytes(), is(new byte[0]));
        assertThat(payment.getArgs(), hasSize(1));
        assertThat(payment.getArgs().get(0).getName(), is("amount"));
        assertThat(payment.getArgs().get(0).getValue().getCLTypeInfo().getType(), is(CLType.U512));
        assertThat(payment.getArgs().get(0).getValue().getBytes(), is(CLValue.fromString("0400ca9a3b")));
        assertThat(payment.getArgs().get(0).getValue().getParsed(), is("1000000000"));
    }

    @Test
    void testDeploySessionFromJson() {
        assertThat(deploy.getSession(), is(instanceOf(DeployExecutable.class)));
    }

    @Test
    void testDeployApprovalsFromJson() {
        assertThat(deploy.getApprovals().size(), is(1));
    }
}
