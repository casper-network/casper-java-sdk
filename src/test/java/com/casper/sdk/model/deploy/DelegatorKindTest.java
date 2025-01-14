package com.casper.sdk.model.deploy;

import com.casper.sdk.model.AbstractJsonTests;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.uref.URef;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

/**
 * @author ian@meywood.com
 */
class DelegatorKindTest extends AbstractJsonTests {

    @Test
    void delegatorKindPurse() throws Exception {
        final String json = "{\"Purse\":\"de0ef381c12dc842e0872453d35aa4b49fa488c0427e68b7dd5ac63e151eae98\"}";

        final DelegatorKind delegatorKind = new ObjectMapper().readValue(json, DelegatorKind.class);

        assertThat(delegatorKind, is(instanceOf(DelegatorKindPurse.class)));
        assertThat(((DelegatorKindPurse) delegatorKind).getPurse(), is(URef.fromString("uref-de0ef381c12dc842e0872453d35aa4b49fa488c0427e68b7dd5ac63e151eae98-000")));

        final String writtenJson = getPrettyJson(delegatorKind);

        JSONAssert.assertEquals(json, writtenJson, JSONCompareMode.NON_EXTENSIBLE);
    }

    @Test
    void delegatorKindPublicKey() throws Exception {

        final String json = "{\"PublicKey\": \"015adadaecbd299c594821a548d755f51a5e2124fb17983f9afae019add32beb21\"}";

        final DelegatorKind delegatorKind = new ObjectMapper().readValue(json, DelegatorKind.class);

        assertThat(delegatorKind, is(instanceOf(DelegatorKindPublicKey.class)));
        assertThat(((DelegatorKindPublicKey) delegatorKind).getPublicKey(), is(PublicKey.fromTaggedHexString("015adadaecbd299c594821a548d755f51a5e2124fb17983f9afae019add32beb21")));

        final String writtenJson = getPrettyJson(delegatorKind);

        JSONAssert.assertEquals(json, writtenJson, JSONCompareMode.NON_EXTENSIBLE);
    }
}
