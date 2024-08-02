package com.casper.sdk.model.key;

import dev.oak3.sbs4j.DeserializerBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ByteCodeKey extends Key {

    private ByteCodeAddr byteCodeAddr;

    protected void deserializeCustom(final DeserializerBuffer deser) throws Exception {
        this.setKey(deser.readByteArray(33));
        this.byteCodeAddr = ByteCodeAddr.getByTag(this.getKey()[0]);
    }
}
