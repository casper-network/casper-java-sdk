package com.casper.sdk.model.transaction;

import com.casper.sdk.jackson.serializer.TransactionHashJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * A legacy deploy transaction hash.
 *
 * @author ian@meywood.com
 */
@JsonSerialize(using = TransactionHashJsonSerializer.class)
public class TransactionHashDeploy extends TransactionHash {

    public TransactionHashDeploy(final String digest) {
        super(digest);
    }

    public TransactionHashDeploy() {
    }
}
