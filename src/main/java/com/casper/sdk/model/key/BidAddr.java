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
    DELEGATOR_ACCOUNT((byte) 2),
    DELEGATOR_PURSE((byte) 3),
    /// Validator credit BidAddr.
    CREDIT((byte) 4),
    RESERVED_DELEGATION_ACCOUNT((byte) 5),
    RESERVED_DELEGATION_PURSE((byte) 6),
    UNBOND_ACCOUNT((byte) 7),
    UNBOND_PURSE((byte) 8);

    private final byte byteTag;

    public static BidAddr getByTag(final byte tag) throws NoSuchKeyTagException {
        for (BidAddr a : values()) {
            if (a.byteTag == tag)
                return a;
        }
        throw new NoSuchKeyTagException();
    }
}
