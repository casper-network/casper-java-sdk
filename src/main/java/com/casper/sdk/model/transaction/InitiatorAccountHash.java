package com.casper.sdk.model.transaction;

import com.casper.sdk.model.common.Digest;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.NoArgsConstructor;

/**
 * @author ian@meywood.com
 */
@NoArgsConstructor(force = true)
public class InitiatorAccountHash extends InitiatorAddr<Digest> {

    @JsonCreator
    public InitiatorAccountHash(final Digest address) {
        super(address);
    }

}
