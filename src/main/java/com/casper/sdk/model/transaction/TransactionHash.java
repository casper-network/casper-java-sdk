package com.casper.sdk.model.transaction;

import com.casper.sdk.model.common.Digest;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * The hash of a transaction
 *
 * @author ian@meywood.com
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TransactionHashDeploy.class, name = "Deploy"),
        @JsonSubTypes.Type(value = TransactionHashV1.class, name = "Version1")})
@Getter
@Setter
public abstract class TransactionHash extends Digest {

    protected TransactionHash(final String digest) {
        super(digest);
    }

    protected TransactionHash() {
    }
}
