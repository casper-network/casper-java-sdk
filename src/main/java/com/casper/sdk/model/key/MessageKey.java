package com.casper.sdk.model.key;

import com.casper.sdk.exception.NoSuchKeyTagException;
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
    public static final String TOPIC = "topic";

    // TODO Change to addressable entity key
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

    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder(getTag().getKeyName());
        if (messageIndex == null) {
            builder.append(TOPIC).append('-');
        }

        builder.append("entity")
                .append('-')
                .append(entityAddr.getKeyName())
                .append('-')
                .append(entityAddrHash)
                .append('-')
                .append(topicHash);

        if (messageIndex != null) {
            builder.append('-').append(String.format("%x", messageIndex));
        }

        return builder.toString();
    }

    @Override
    protected void fromStringCustom(final String strKey) {

        final SerializerBuffer serializerBuffer = new SerializerBuffer();

        final String[] split = strKey.split("-");

        if (TOPIC.equals(split[1])) {
            if (split.length != 6) {
                throw new IllegalArgumentException("Invalid message key: " + strKey);
            }

            try {
                entityAddr = EntityAddr.getByKeyName(split[3]);
            } catch (NoSuchKeyTagException e) {
                throw new IllegalArgumentException(e);
            }
            entityAddrHash = new Digest(split[4]);
            topicHash = new Digest(split[5]);
        } else {
            try {
                entityAddr = EntityAddr.getByKeyName(split[2]);
            } catch (NoSuchKeyTagException e) {
                throw new IllegalArgumentException(e);
            }
            entityAddrHash = new Digest(split[3]);
            topicHash = new Digest(split[4]);

            if (split.length > 5) {
                messageIndex = Long.parseLong(split[5], 16);
            }
        }

        serializerBuffer.writeU8(entityAddr.getByteTag());
        serializerBuffer.writeByteArray(entityAddrHash.getDigest());
        serializerBuffer.writeByteArray(topicHash.getDigest());
        if (messageIndex != null) {
            serializerBuffer.writeBool(true);
            serializerBuffer.writeU32(messageIndex);
        } else {
            serializerBuffer.writeBool(false);
        }

        setKey(serializerBuffer.toByteArray());
    }


}
