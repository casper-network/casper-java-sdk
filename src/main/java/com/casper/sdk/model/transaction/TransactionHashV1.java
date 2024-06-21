package com.casper.sdk.model.transaction;

import com.casper.sdk.jackson.serializer.TransactionHashJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * A Version1 transaction hash.
 *
 * @author ian@meywood.com
 */
@JsonSerialize(using = TransactionHashJsonSerializer.class)
public class TransactionHashV1 extends TransactionHash {

    public TransactionHashV1(final String digest) {
        super(digest);
    }

    public TransactionHashV1() {
    }
}
