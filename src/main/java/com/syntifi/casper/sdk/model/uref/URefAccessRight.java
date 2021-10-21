package com.syntifi.casper.sdk.model.uref;

import com.syntifi.casper.sdk.exception.DynamicInstanceException;
import com.syntifi.casper.sdk.model.clvalue.CLValueURef;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Casper CLValue URef access rights definitons
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see CLValueURef 
 * @since 0.0.1
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum URefAccessRight {
    NONE((byte)0x0),
    READ((byte)0x1),
    WRITE((byte)0x2),
    READ_WRITE((byte)0x3),
    ADD((byte)0x4),
    READ_ADD((byte)0x5),
    ADD_WRITE((byte)0x6),
    READ_ADD_WRITE((byte)0x7);

    public final byte serializationTag;

    public static URefAccessRight getTypeBySerializationTag(byte serializationTag) throws DynamicInstanceException {
        for (URefAccessRight accessRight: values()) {
            if (accessRight.serializationTag == serializationTag) {
                return accessRight;
            }
        }
        throw new DynamicInstanceException("No such access right exception");
    }
}
