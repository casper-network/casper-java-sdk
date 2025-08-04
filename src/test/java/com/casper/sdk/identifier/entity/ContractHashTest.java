package com.casper.sdk.identifier.entity;

import com.casper.sdk.exception.NoSuchKeyTagException;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.key.Key;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author ian@meywood.com
 */
class ContractHashTest {


    @Test
    void contactHash() throws NoSuchKeyTagException {

        ContractHash contractHash = new ContractHash("hash-0c6464a101d2aa0be83e83ea3daf3e4741e9b2bd1a98dbb8c8058a49c6846142");
        assertThat(contractHash.toString(), is("contract-0c6464a101d2aa0be83e83ea3daf3e4741e9b2bd1a98dbb8c8058a49c6846142"));

        assertThat(contractHash, is(new ContractHash(new Digest("0c6464a101d2aa0be83e83ea3daf3e4741e9b2bd1a98dbb8c8058a49c6846142"))));
        assertThat(contractHash, is(new ContractHash(Key.create("hash-0c6464a101d2aa0be83e83ea3daf3e4741e9b2bd1a98dbb8c8058a49c6846142"))));
    }
}
