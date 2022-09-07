package com.casper.sdk.model.key;

import com.casper.sdk.model.clvalue.serde.CasperSerializableObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.util.ByteUtils;
import lombok.*;

/**
 * Hex-encoded key, including the tag byte.
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"tag", "key"})
public abstract class AbstractSerializedKeyTaggedHex<T extends Tag> implements CasperSerializableObject {

    /**
     * @see Tag
     */
    @JsonIgnore
    private T tag;

    /**
     * Hex-encoded key
     */
    @JsonIgnore
    private byte[] key;

    @JsonValue
    public String getAlgoTaggedHex() {
        return ByteUtils.encodeHexString(new byte[]{this.tag.getByteTag()})
                + ByteUtils.encodeHexString(this.getKey());
    }

    /**
     * Implements TaggedHEx encoder
     */
    @Override
    public void serialize(SerializerBuffer ser, boolean encodeType) {
        ser.writeU8(getTag().getByteTag());
        ser.writeByteArray(getKey());
    }
}
