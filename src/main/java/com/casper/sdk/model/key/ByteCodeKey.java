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
                + ByteUtils.encodeHexString(this.getKey());
    }

    @Override
    protected void fromStringCustom(final String strKey) {
        this.byteCodeAddr = ByteCodeAddr.getByKeyName(strKey);
        final byte[] key = new byte[33];
        final String[] split = strKey.split("-");
        key[0] = byteCodeAddr.getByteTag();
        System.arraycopy(Hex.decode(split[split.length - 1]), 0, key, 1, 32);
        setKey(key);
    }

    @Override
    public String toString() {
        return this.getTag().getKeyName() + byteCodeAddr.getKeyName() + "-" + Hex.encode(this.getKey()).substring(2);
    }

    protected void deserializeCustom(final DeserializerBuffer deser) throws Exception {
        this.setKey(deser.readByteArray(33));
        this.byteCodeAddr = ByteCodeAddr.getByTag(this.getKey()[0]);
    }
}
