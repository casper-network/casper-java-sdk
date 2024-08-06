package com.casper.sdk.model.key;

import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.entity.EntityAddr;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import lombok.*;

import java.util.Optional;

/**
 * A `Key` under which a message is stored.
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MessageKey extends Key {

    /** The entity addr. */
    private EntityAddr entityAddr;
    /** The hash of the entity address. */
    private Digest entityAddrHash;
    /** The optional message index. */
    @Getter(AccessLevel.NONE)
    private Long messageIndex;
    /** The hash of the name of the message topic. */
    private Digest topicHash;

    @Override
    protected void deserializeCustom(final DeserializerBuffer deser) throws Exception {

        // TODO create objects from bytes
        final SerializerBuffer serializerBuffer = new SerializerBuffer();

        // EntityAddr key entity_addr - inc has
        entityAddr = EntityAddr.getByTag(deser.readU8());
        serializerBuffer.writeU8(entityAddr.getByteTag());

        // EntityAddr Hash entity_addr_hash
        final byte[] entityAddrHash = deser.readByteArray(32);
        serializerBuffer.writeByteArray(entityAddrHash);
        this.entityAddrHash = new Digest();
        this.entityAddrHash.setDigest(entityAddrHash);

        // Topic name Hash topic_name_hash
        final byte[] topicNameHash = deser.readByteArray(32);
        serializerBuffer.writeByteArray(topicNameHash);
        this.topicHash = new Digest();
        this.topicHash.setDigest(topicNameHash);

        // Optional U32 message_index
        final Boolean isMessageIndexPresent = deser.readBool();
        serializerBuffer.writeBool(isMessageIndexPresent);
        if (isMessageIndexPresent) {
            messageIndex = deser.readU32();
            serializerBuffer.writeU32(messageIndex);
        }

        setKey(serializerBuffer.toByteArray());
    }

    public Optional<Long> getMessageIndex() {
        return Optional.ofNullable(messageIndex);
    }
}
