package com.casper.sdk.model.entity;

import com.casper.sdk.exception.NoSuchKeyTagException;
import com.casper.sdk.model.key.KeyTag;
import com.casper.sdk.model.key.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The byte tags for the different types of entity addresses.
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@Getter
public enum EntityAddr implements Tag {
    /** The address for a system entity account or contract. */
    SYSTEM((byte) 0, "system"),
    /** The address of an entity that corresponds to an Account. */
    ACCOUNT((byte) 1, "account"),
    /** The address of an entity that corresponds to a Userland smart contract. */
    SMART_CONTRACT((byte) 2, "contract");

    private final byte byteTag;
    private final String keyName;

    public static EntityAddr getByTag(byte tag) throws NoSuchKeyTagException {
        for (EntityAddr a : values()) {
            if (a.byteTag == tag)
                return a;
        }
        throw new NoSuchKeyTagException();
    }

    public static EntityAddr getByKeyName(final String keyName) throws NoSuchKeyTagException {
        // Search in reverse order to get the most specific key eg 'bid-addr-' and 'bid-'
        for (final EntityAddr entityAddr: values()) {
           if (entityAddr.getKeyName().equals(keyName)) {
               return entityAddr;
           }
        }
        throw new NoSuchKeyTagException("No such key name: " + keyName);
    }

}
