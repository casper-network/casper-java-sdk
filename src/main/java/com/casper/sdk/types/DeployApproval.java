package com.casper.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A digital signature approving deploy processing.
 */
public class DeployApproval {

    /** The public key component to the signing key used to sign a deploy. */
    private final CLPublicKey signer;
    /** The digital signature signalling approval of deploy processing. */
    private final Signature signature;

    @JsonCreator
    public DeployApproval(@JsonProperty("signer") final CLPublicKey signer,
                          @JsonProperty("signature") final Signature signature) {
        this.signer = signer;
        this.signature = signature;
    }


    public CLPublicKey getSigner() {
        return signer;
    }

    public Signature getSignature() {
        return signature;
    }
}
