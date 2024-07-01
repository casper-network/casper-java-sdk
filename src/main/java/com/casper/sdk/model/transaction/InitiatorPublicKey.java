package com.casper.sdk.model.transaction;

import com.casper.sdk.model.key.PublicKey;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.NoArgsConstructor;

/**
 * The public key of the account that initiated the contract call.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
public class InitiatorPublicKey extends InitiatorAddr<PublicKey> {

    @JsonCreator
    public InitiatorPublicKey(final PublicKey address) {
        super(address);
    }


    @Override
    public byte getByteTag() {
        return PUBLIC_KEY_TAG;
    }
}
