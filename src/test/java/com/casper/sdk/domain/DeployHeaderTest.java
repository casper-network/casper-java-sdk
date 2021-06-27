package com.casper.sdk.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;

class DeployHeaderTest {

    private DeployHeader deployHeader;

    @BeforeEach
    void setUp() {

        deployHeader = new DeployHeader(
                new PublicKey("017f747b67bd3fe63c2a736739dfe40156d622347346e70f68f51c178a75ce5537"),
                "2021-05-04T14:20:35.104Z",
                "30m",
                20,
                new Digest("f2e0782bba4a0a9663cafc7d707fd4a74421bc5bfef4e368b7e8f38dfab87db8"),
                Arrays.asList(
                        new Digest("0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f"),
                        new Digest("1010101010101010101010101010101010101010101010101010101010101010")
                ),
                "mainnet"
        );
    }

    @Test
    void getAccount() {

        assertThat(deployHeader.getAccount().getBytes(), is(PublicKey.fromString("7f747b67bd3fe63c2a736739dfe40156d622347346e70f68f51c178a75ce5537")));
        assertThat(deployHeader.getAccount().getKeyAlgorithm(), is(KeyAlgorithm.ED25519));
    }

    @Test
    void getTimestamp() {
        assertThat(deployHeader.getTimestamp(), is(1620138035104L));
    }

    @Test
    void getTtl() {
        assertThat(deployHeader.getTtl(), is(1800000L));
    }

    @Test
    void getGasPrice() {
        assertThat(deployHeader.getGasPrice(), is(20));
    }

    @Test
    void getBodyHash() {
        assertThat(deployHeader.getBodyHash(), is(new Digest("f2e0782bba4a0a9663cafc7d707fd4a74421bc5bfef4e368b7e8f38dfab87db8")));
    }

    @Test
    void getDependencies() {
        assertThat(deployHeader.getDependencies(),
                hasItems(
                        new Digest("0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f"),
                        new Digest("1010101010101010101010101010101010101010101010101010101010101010")
                )
        );
    }

    @Test
    void getChainName() {
        assertThat(deployHeader.getChainName(), is("mainnet"));
    }
}
