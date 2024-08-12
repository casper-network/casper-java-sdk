package com.casper.sdk.model.key;

import com.casper.sdk.exception.NoSuchKeyTagException;
import com.casper.sdk.model.entity.EntityAddr;
import com.fasterxml.jackson.annotation.JsonValue;
import com.syntifi.crypto.key.encdec.Hex;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.util.ByteUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A key for an Addressable entity.
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AddressableEntityKey extends Key {

    private EntityAddr entityAddressTag;

    @Override
    protected void fromStringCustom(final String strKey) {
        try {
            final String[] split = strKey.split("-");
            entityAddressTag = EntityAddr.getByKeyName(split[1]);
            final byte[] key = new byte[33];
            key[0] = entityAddressTag.getByteTag();
            System.arraycopy(Hex.decode(split[split.length - 1]), 0, key, 1, 32);
            setKey(key);
        } catch (NoSuchKeyTagException e) {
            throw new IllegalArgumentException("Invalid key: " + strKey, e);
        }
    }

    protected void deserializeCustom(final DeserializerBuffer deser) throws Exception {
        this.setTag(KeyTag.ADDRESSABLE_ENTITY);
        this.setKey(deser.readByteArray(33));
        this.entityAddressTag = EntityAddr.getByTag(getKey()[0]);
    }

    @Override
    public String toString() {
        return this.getTag().getKeyName() + entityAddressTag.getKeyName() + "-" + Hex.encode(this.getKey()).substring(2);
    }

    @JsonValue
    public String getAlgoTaggedHex() {
        return ByteUtils.encodeHexString(new byte[]{this.getTag().getByteTag()})
                + ByteUtils.encodeHexString(this.getKey());
    }
}
