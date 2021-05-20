package com.casper.sdk.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Unit tests for the {@link Deploy} class
 */
class DeployTest {

    /** The Deploy to test */
    private Deploy deploy;

    @BeforeEach
    void setUp() {
        deploy = new Deploy(
                new Digest("hash"),
                new DeployHeader(
                        new PublicKey("017f747b67bd3fe63c2a736739dfe40156d622347346e70f68f51c178a75ce5537")
                        , "2021-05-04T14:20:35.104Z",
                        "30m",
                        new Digest("f2e0782bba4a0a9663cafc7d707fd4a74421bc5bfef4e368b7e8f38dfab87db8"),
                        new ArrayList<>(),
                        "mainnet"
                ),
                // Payment
                new DeployExecutable(Arrays.asList(
                        new DeployNamedArg("foo", new CLValue(new byte[]{1}, CLType.BOOL)),
                        new DeployNamedArg("bar", new CLValue("Bar".getBytes(), CLType.STRING))
                )),
                // Session
                new DeployExecutable((Arrays.asList(
                        new DeployNamedArg("oh", new CLValue(toBytes(Long.MAX_VALUE), CLType.I64)),
                        new DeployNamedArg("no", new CLValue(toBytes(Integer.MAX_VALUE), CLType.I32)))
                )),
                new HashSet<>()
        );
    }

    @Test
    void getHash() {
        assertThat(deploy.getHash(), is(notNullValue(Digest.class)));
        assertThat(deploy.getHash().getHash(), is("hash"));
    }

    @Test
    void getHeader() {
        assertThat(deploy.getHeader(), is(notNullValue(DeployHeader.class)));
        assertThat(deploy.getHeader().getChainName(), is("mainnet"));
    }

    @Test
    void getPayment() {
        assertThat(deploy.getPayment(), is(notNullValue(DeployExecutable.class)));
    }

    @Test
    void getSession() {
        assertThat(deploy.getSession(), is(notNullValue(DeployExecutable.class)));
    }


    @Test
    void getApprovals() {
        assertThat(deploy.getApprovals(), is(notNullValue(Set.class)));
    }

    byte[] toBytes(final long number) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(number);
        return buffer.array();
    }

    byte[] toBytes(final int number) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(number);
        return buffer.array();
    }

}