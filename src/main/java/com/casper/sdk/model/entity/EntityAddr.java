package com.casper.sdk.model.entity;

import com.casper.sdk.exception.NoSuchKeyTagException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The byte tags for the different types of entity addresses.
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@Getter
public enum EntityAddr {
    /** The address for a system entity account or contract. */
    SYSTEM((byte) 0),
    /** The address of an entity that corresponds to an Account. */
    ACCOUNT((byte) 1),
    /** The address of an entity that corresponds to a Userland smart contract. */
    SMART_CONTRACT((byte) 2);

    private final byte byteTag;

    public static EntityAddr getByTag(byte tag) throws NoSuchKeyTagException {
        for (EntityAddr a : values()) {
            if (a.byteTag == tag)
                return a;
        }
        throw new NoSuchKeyTagException();
    }
}
