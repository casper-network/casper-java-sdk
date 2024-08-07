package com.casper.sdk.model.key;

import dev.oak3.sbs4j.DeserializerBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *  A `Key` under which a byte code record is stored.
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ByteCodeKey extends Key {

    /** A tag for an address for ByteCode records stored in global state. */
    private ByteCodeAddr byteCodeAddr;

    protected void deserializeCustom(final DeserializerBuffer deser) throws Exception {
        this.setKey(deser.readByteArray(33));
        this.byteCodeAddr = ByteCodeAddr.getByTag(this.getKey()[0]);
    }
}
