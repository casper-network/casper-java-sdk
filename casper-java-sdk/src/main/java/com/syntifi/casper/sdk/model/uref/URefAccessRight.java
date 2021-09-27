package com.syntifi.casper.sdk.model.uref;

import com.syntifi.casper.sdk.exception.DynamicInstanceException;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueURef;

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
    NONE(0x0),
    READ(0x1),
    WRITE(0x2),
    READ_WRITE(0x3),
    ADD(0x4),
    READ_ADD(0x5),
    ADD_WRITE(0x6),
    READ_ADD_WRITE(0x7);

    public final int serializationTag;

    public static URefAccessRight getTypeBySerializationTag(byte serializationTag) throws DynamicInstanceException {
        for (URefAccessRight accessRight: values()) {
            if (accessRight.serializationTag == serializationTag) {
                return accessRight;
            }
        }
        throw new DynamicInstanceException("No such access right exception");
    }

}
