package com.casper.sdk.model.key;

import com.casper.sdk.exception.NoSuchKeyTagException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Bid Address
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@Getter
public enum BidAddr {
    /// Unified BidAddr.
    UNIFIED((byte) 0),
    /// Validator BidAddr.
    VALIDATOR((byte) 1),
    /// Delegator BidAddr.
    DELEGATOR((byte) 2),
    /// Validator credit BidAddr.
    CREDIT((byte) 4);

    private final byte byteTag;

    public static BidAddr getByTag(byte tag) throws NoSuchKeyTagException {
        for (BidAddr a : values()) {
            if (a.byteTag == tag)
                return a;
        }
        throw new NoSuchKeyTagException();
    }
}
