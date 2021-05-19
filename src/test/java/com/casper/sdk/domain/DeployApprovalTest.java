package com.casper.sdk.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Unit tests the DeployApproval domain object
 */
class DeployApprovalTest {

    private DeployApproval deployApproval;

    @BeforeEach
    void setUp() {

        deployApproval = new DeployApproval(
                new PublicKey("I'm a key".getBytes()),
                new Signature("0102030405")
        );
    }

    @Test
    void getSigner() {
        assertThat(deployApproval.getSigner(), is(notNullValue(PublicKey.class)));
        assertThat(deployApproval.getSigner().getBytes(), is("I'm a key".getBytes()));
    }

    @Test
    void getSignature() {
        assertThat(deployApproval.getSignature(), is(notNullValue(Signature.class)));
        // TODO with some real data
    }
}