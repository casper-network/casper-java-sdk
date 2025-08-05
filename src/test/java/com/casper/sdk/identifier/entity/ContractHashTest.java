package com.casper.sdk.identifier.entity;

import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.key.Key;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author ian@meywood.com
 */
class ContractHashTest {

    public static final String HASH = "010c6464a101d2aa0be83e83ea3daf3e4741e9b2bd1a98dbb8c8058a49c6846142";

    @Test
    void contactHash() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        ContractHash contractHash = new ContractHash("hash-" + HASH);
        assertThat(contractHash.toString(), is("contract-" + HASH));

        assertThat(contractHash, is(new ContractHash(new Digest(HASH))));
        assertThat(contractHash, is(new ContractHash(Key.create("hash-" + HASH))));

        assertThat(contractHash.asDigest(), is(new Digest(HASH)));


        String written = objectMapper.writeValueAsString(contractHash);
        assertThat(written, is("{\"ContractHash\":\"" + contractHash + "\"}"));

        assertThat(contractHash.getHash(), is(HASH));

       final ContractHash read = objectMapper.readValue(written, ContractHash.class);
        assertThat(read, is(contractHash));
    }
}
