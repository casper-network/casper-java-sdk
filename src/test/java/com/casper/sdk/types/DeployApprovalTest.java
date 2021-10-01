package com.casper.sdk.types;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Unit tests the DeployApproval type object
 */
class DeployApprovalTest {

    private DeployApproval deployApproval;

    @BeforeEach
    void setUp() {

        deployApproval = new DeployApproval(
                new PublicKey("12345678901234567890123456789012".getBytes()),
                new Signature("0195a68b1a05731b7014e580b4c67a506e0339a7fffeaded9f24eb2e7f78b96bdd900b9be8ca33e4552a9a619dc4fc5e4e3a9f74a4b0537c14a5a8007d62a5dc06")
        );
    }

    @Test
    void getSigner() {
        assertThat(deployApproval.getSigner(), is(notNullValue(PublicKey.class)));
        assertThat(deployApproval.getSigner().getBytes(), is("12345678901234567890123456789012".getBytes()));
    }

    @Test
    void getSignature() {
        assertThat(deployApproval.getSignature(), is(notNullValue(Signature.class)));
        assertThat(deployApproval.getSignature().getBytes(), is(CLValue.fromString("95a68b1a05731b7014e580b4c67a506e0339a7fffeaded9f24eb2e7f78b96bdd900b9be8ca33e4552a9a619dc4fc5e4e3a9f74a4b0537c14a5a8007d62a5dc06")));
        assertThat(deployApproval.getSignature().getKeyAlgorithm(), is(SignatureAlgorithm.ED25519));
    }
}
