package com.casper.sdk.model.key;

import com.fasterxml.jackson.annotation.JsonValue;
import com.syntifi.crypto.key.encdec.Hex;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.util.ByteUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A `Key` under which a byte code record is stored.
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ByteCodeKey extends Key {

    /** A tag for an address for ByteCode records stored in global state. */
    private ByteCodeAddr byteCodeAddr;

    @JsonValue
    public String getAlgoTaggedHex() {
        return ByteUtils.encodeHexString(new byte[]{this.getTag().getByteTag()})
                + ByteUtils.encodeHexString(new byte[]{this.getByteCodeAddr().getByteTag()})
                + ByteUtils.encodeHexString(this.getKey());
    }

    @Override
    protected void fromStringCustom(String strKey) {
        super.fromStringCustom(strKey);
        this.byteCodeAddr = ByteCodeAddr.getByKeyName(strKey);
    }

    @Override
    public String toString() {
        return this.getTag().getKeyName() + byteCodeAddr.getKeyName() + "-" + Hex.encode(this.getKey());
    }

    protected void deserializeCustom(final DeserializerBuffer deser) throws Exception {
        this.setKey(deser.readByteArray(33));
        this.byteCodeAddr = ByteCodeAddr.getByTag(this.getKey()[0]);
    }

}
