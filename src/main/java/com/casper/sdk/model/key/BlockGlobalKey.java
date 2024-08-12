package com.casper.sdk.model.key;

import com.syntifi.crypto.key.encdec.Hex;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A `Key` under which per-block details are stored to global state.
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BlockGlobalKey extends Key {

    @Override
    public String toString() {
        return getTag().getKeyName() + blockGlobalAddr.getKeyName() + "-" + Hex.encode(getKey()).substring(2);
    }

    private BlockGlobalAddr blockGlobalAddr;

    @Override
    protected void fromStringCustom(final String strKey) {
        blockGlobalAddr = BlockGlobalAddr.getByKeyName(strKey);
        super.fromStringCustom(strKey);
        SerializerBuffer ser = new SerializerBuffer();
        ser.writeU8(blockGlobalAddr.getByteTag());
        final String[] split = strKey.split("-");
        ser.writeByteArray(Hex.decode(split[split.length-1]));
        setKey(ser.toByteArray());
    }

    @Override
    protected void deserializeCustom(final DeserializerBuffer deser) throws Exception {
        this.setTag(KeyTag.BLOCK_GLOBAL);
        this.setKey(deser.readByteArray(33));
        this.blockGlobalAddr = BlockGlobalAddr.getByTag(getKey()[0]);
    }
}
