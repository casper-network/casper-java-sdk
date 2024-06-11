package com.casper.sdk.model.era;

import com.casper.sdk.model.key.PublicKey;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Unit tests for {@link EraEndV2}.
 *
 * @author ian@meywood.com
 */
class EraEndV2Test {

    @Test
    void parseEraEndV2() throws IOException, NoSuchAlgorithmException {

        final InputStream jsonIn = Objects.requireNonNull(getClass().getResource("/era-info-samples/era_end_v2.json")).openStream();

        //noinspection VulnerableCodeUsages
        final EraEndV2 eraEndV2 = new ObjectMapper().readValue(jsonIn, EraEndV2.class);

        assertThat(eraEndV2, is(notNullValue()));

        assertThat(eraEndV2.getEquivocators().size(), is(3));
        assertThat(eraEndV2.getEquivocators().get(0), is(PublicKey.fromTaggedHexString("01385e7e800dfcbf0abf1454d8e5300a09a6a42d7ca6d15bf9f45f932f2696bc0d")));
        assertThat(eraEndV2.getEquivocators().get(2), is(PublicKey.fromTaggedHexString("01ca84d8c6ea00653473da0f46642f49e8465c82aa2ff281100e2002a9d70f51d7")));

        assertThat(eraEndV2.getInactiveValidators().size(), is(3));
        assertThat(eraEndV2.getInactiveValidators().get(1), is(PublicKey.fromTaggedHexString("01aed5e394a3e59b77ca6af6c97d584e0aa8d4ee40c4e51b27b635ec255f9ce1bd")));
        assertThat(eraEndV2.getInactiveValidators().get(2), is(PublicKey.fromTaggedHexString("01e71a498829ab4247d2d48022d95e23374c2138b184ebb1bc0bc4a45c5b24f9d5")));

        assertThat(eraEndV2.getNextEraValidatorWeights().size(), is(5));
        assertThat(eraEndV2.getNextEraValidatorWeights().get(0).getValidator(), is(PublicKey.fromTaggedHexString("010b277da84a12c8814d5723eeb57123ff287f22466fd13faca1bb1fae57d2679b")));
        assertThat(eraEndV2.getNextEraValidatorWeights().get(0).getWeight(), is(new BigInteger("11823890605832274469")));
        assertThat(eraEndV2.getNextEraValidatorWeights().get(4).getValidator(), is(PublicKey.fromTaggedHexString("01f6e6e82759da039d479900ad1b930320082d1fc00aea6f5e5f7289c7a6c90536")));
        assertThat(eraEndV2.getNextEraValidatorWeights().get(4).getWeight(), is(new BigInteger("11990568590760997935")));

        assertThat(eraEndV2.getRewards().size(), is(5));
        assertThat(
                eraEndV2.getRewards().get(PublicKey.fromTaggedHexString("010b277da84a12c8814d5723eeb57123ff287f22466fd13faca1bb1fae57d2679b")),
                hasItems(new BigInteger("4026058477024681"), new BigInteger("402627925137076"))
        );
        assertThat(
                eraEndV2.getRewards().get(PublicKey.fromTaggedHexString("01f6e6e82759da039d479900ad1b930320082d1fc00aea6f5e5f7289c7a6c90536")),
                hasItems(new BigInteger("13081818953982388"), new BigInteger("408309291908807"))
        );

        assertThat(eraEndV2.getNextEraGasPrice(), is(2));
    }
}
