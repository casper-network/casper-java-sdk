package com.casper.sdk.how_to;

import com.casper.sdk.CasperSdk;
import com.casper.sdk.KeyPairStreams;
import com.casper.sdk.types.Deploy;
import com.casper.sdk.types.DeployParams;
import com.casper.sdk.types.Digest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.KeyPair;
import java.time.Instant;
import java.util.Random;

import static com.casper.sdk.how_to.HowToUtils.getNodeKeyPair;
import static com.casper.sdk.how_to.HowToUtils.getUserKeyPairStreams;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@Disabled // Remove this to run locally
public class HowToTransferToValidator {

    /** Create new instance of the SDK with default NCTL url and port */
    final CasperSdk casperSdk = new CasperSdk("http://localhost", 40101);

    @Test
    public void testTransferToValidator() throws IOException {

        final KeyPairStreams nodeKeyOneStream = getNodeKeyPair(1);
        final KeyPair delegator = casperSdk.loadKeyPair(nodeKeyOneStream.getPublicKeyIn(), nodeKeyOneStream.getPrivateKeyIn());

        final KeyPairStreams counterPartyTwoStream = getUserKeyPairStreams(2);
        final KeyPair counterPartyTwoKeyPair = casperSdk.loadKeyPair(counterPartyTwoStream.getPublicKeyIn(), counterPartyTwoStream.getPrivateKeyIn());
        final Number amount = 2.5e9;
        final int correlationId = new Random().nextInt();

        final Deploy deploy = casperSdk.makeNativeTransfer(
                new DeployParams(
                        delegator.getPublic(),
                        "casper-net-1",
                        10,
                        Instant.now().toEpochMilli(),
                        DeployParams.DEFAULT_TTL,
                        null),
                amount,
                counterPartyTwoKeyPair.getPublic(),
                correlationId
        );

        casperSdk.signDeploy(deploy, delegator);
        casperSdk.signDeploy(deploy, counterPartyTwoKeyPair);

        final String json = casperSdk.deployToJson(deploy);
        assertThat(json, is(notNullValue()));

        final Digest digest = casperSdk.putDeploy(deploy);
        assertThat(digest, is(notNullValue()));
    }
}
