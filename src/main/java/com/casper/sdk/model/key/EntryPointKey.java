package com.casper.sdk.model.key;

import com.casper.sdk.exception.NoSuchKeyTagException;
import com.casper.sdk.model.entity.EntityAddr;
import com.syntifi.crypto.key.encdec.Hex;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A `Key` under which a entrypoint record is written.
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EntryPointKey extends Key {

    /** The addr of the entity. */
    private EntryPointAddr entryPointAddr;
    /** The entry point address. */
    private EntityAddr entityAddr;
    /** The 32 byte hash of the name of the entry point. */
    private byte[] hashAddr;
    /** The 32 byte hash of the name of the V1 entry point */
    private byte[] namedBytes;
    /** The selector for a V2 entry point. */
    private long selector;


    @Override
    protected void fromStringCustom(final String strKey) {
        final String[] split = strKey.split("-");
        try {
            this.entryPointAddr = EntryPointAddr.getByKeyName(split[2]);
            this.entityAddr = EntityAddr.getByKeyName(split[4]);
        } catch (NoSuchKeyTagException e) {
            throw new IllegalArgumentException("Invalid key: " + strKey);
        }

        this.hashAddr = Hex.decode(split[5]);

        if (this.entryPointAddr == EntryPointAddr.VM_CASPER_V1) {
            this.namedBytes = Hex.decode(split[6]);
        } else if (this.entryPointAddr == EntryPointAddr.VM_CASPER_V2) {
            this.selector = Long.parseLong(split[6]);
        } else {
            throw new IllegalArgumentException("Invalid key: " + strKey);
        }
    }

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder(getTag().getKeyName());

        if (this.entryPointAddr == EntryPointAddr.VM_CASPER_V2) {
            sb.append("v2");
        } else {
            sb.append("v1");
        }
        sb.append("-entity-")
                .append(entityAddr.getKeyName())
                .append('-')
                .append(Hex.encode(this.hashAddr))
                .append('-');

        if (this.entryPointAddr == EntryPointAddr.VM_CASPER_V2) {
            sb.append(this.selector);
        } else {
            sb.append(Hex.encode(this.namedBytes));
        }

        updateKey();

        return sb.toString();
    }


    @Override
    protected void deserializeCustom(final DeserializerBuffer deser) throws Exception {
        this.entryPointAddr = EntryPointAddr.getByTag(deser.readU8());
        this.entityAddr = EntityAddr.getByTag(deser.readU8());
        this.hashAddr = deser.readByteArray(32);

        if (this.entryPointAddr == EntryPointAddr.VM_CASPER_V1) {
            this.namedBytes = deser.readByteArray(32);
        } else {
            this.selector = deser.readU32();
        }
        updateKey();
    }

    private void updateKey() {
        final SerializerBuffer ser = new SerializerBuffer();
        ser.writeU8(entryPointAddr.getByteTag());
        ser.writeU8(entityAddr.getByteTag());
        ser.writeByteArray(hashAddr);

        if (this.entryPointAddr == EntryPointAddr.VM_CASPER_V1) {
            ser.writeByteArray(this.namedBytes);
        } else {
            ser.writeU32(this.selector);
        }

        this.setKey(ser.toByteArray());
    }
}
