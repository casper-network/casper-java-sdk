package com.casper.sdk.model.transaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syntifi.crypto.key.encdec.Hex;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNot.not;

/**
 * Unit tests for {@link TransactionHash}
 *
 * @author ian@meywood.com
 */
class TransactionHashTest {

    private static final String DEPLOY_JSON = "{\"Deploy\":\"cb04018ad3a09fc15fda0e5c18def392a135652c73c864c25968be9b2376c139\"}";

    @Test
    void constructDeploy() throws JsonProcessingException {

        final TransactionHashDeploy transactionHash = new TransactionHashDeploy("cb04018ad3a09fc15fda0e5c18def392a135652c73c864c25968be9b2376c139");
        final String written = new ObjectMapper().writeValueAsString(transactionHash);
        assertThat(written, is(DEPLOY_JSON));
    }

    @Test
    void transactionHashDeploy() throws JsonProcessingException {

        final TransactionHash transactionHash = new ObjectMapper().readValue(DEPLOY_JSON, TransactionHash.class);
        assertThat(transactionHash, is(instanceOf(TransactionHashDeploy.class)));
        assertThat(transactionHash.getDigest(), is(Hex.decode("cb04018ad3a09fc15fda0e5c18def392a135652c73c864c25968be9b2376c139")));

        final String written = new ObjectMapper().writeValueAsString(transactionHash);
        assertThat(written, is(DEPLOY_JSON));
    }

    @Test
    void transactionHashV1() throws JsonProcessingException {
        final String json = "{\"Version1\":\"cb04018ad3a09fc15fda0e5c18def392a135652c73c864c25968be9b2376c139\"}";

        final TransactionHash transactionHash = new ObjectMapper().readValue(json, TransactionHash.class);
        assertThat(transactionHash, is(instanceOf(TransactionHashV1.class)));
        assertThat(transactionHash.getDigest(), is(Hex.decode("cb04018ad3a09fc15fda0e5c18def392a135652c73c864c25968be9b2376c139")));
        assertThat(transactionHash, is(new TransactionHashV1("cb04018ad3a09fc15fda0e5c18def392a135652c73c864c25968be9b2376c139")));
        final String written = new ObjectMapper().writeValueAsString(transactionHash);
        assertThat(written, is(json));

        assertThat(new TransactionV1(), is(not(new TransactionHashV1("cb04018ad3a09fc15fda0e5c18def392a135652c73c864c25968be9b2376c139"))));
    }
}
