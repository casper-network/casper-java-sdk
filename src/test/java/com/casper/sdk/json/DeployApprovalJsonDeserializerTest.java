package com.casper.sdk.json;

import com.casper.sdk.domain.DeployApproval;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests the {@link DeployApproval} can be parsed from JSON
 */
class DeployApprovalJsonDeserializerTest {

    private static final String JSON = """
            {
               "signer": "017f747b67bd3fe63c2a736739dfe40156d622347346e70f68f51c178a75ce5537",
               "signature": "0195a68b1a05731b7014e580b4c67a506e0339a7fffeaded9f24eb2e7f78b96bdd900b9be8ca33e4552a9a619dc4fc5e4e3a9f74a4b0537c14a5a8007d62a5dc06"
            }""";



    private static final String APPROVALS_JSON = """
            [{
               "signer": "017f747b67bd3fe63c2a736739dfe40156d622347346e70f68f51c178a75ce5537",
               "signature": "0195a68b1a05731b7014e580b4c67a506e0339a7fffeaded9f24eb2e7f78b96bdd900b9be8ca33e4552a9a619dc4fc5e4e3a9f74a4b0537c14a5a8007d62a5dc06"
            }]""";


    @Test
    void testParseDeployApprovalFromJson() throws IOException {
        final DeployApproval deployApproval = new ObjectMapper().reader().readValue(JSON, DeployApproval.class);
        assertThat(deployApproval, is(notNullValue()));
        assertThat(deployApproval.getSigner(), is(notNullValue()));
        assertThat(deployApproval.getSignature(), is(notNullValue()));
    }
}
