package com.casper.sdk.model.key;

import com.casper.sdk.model.entity.EntityAddr;
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
    protected void deserializeCustom(final DeserializerBuffer deser) throws Exception {
        this.entryPointAddr = EntryPointAddr.getByTag(deser.readU8());
        this.entityAddr = EntityAddr.getByTag(deser.readU8());
        this.hashAddr = deser.readByteArray(32);

        final SerializerBuffer ser = new SerializerBuffer();
        ser.writeU8(entryPointAddr.getByteTag());
        ser.writeU8(entityAddr.getByteTag());
        ser.writeByteArray(hashAddr);

        if (this.entryPointAddr == EntryPointAddr.VM_CASPER_V1) {
            this.namedBytes = deser.readByteArray(32);
            ser.writeByteArray(this.namedBytes);
        } else {
            this.selector = deser.readU32();
            ser.writeU32(this.selector);
        }

        this.setKey(ser.toByteArray());
    }
}
