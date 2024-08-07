package com.casper.sdk.model.key;

import com.casper.sdk.exception.NoSuchKeyTagException;
import com.casper.sdk.jackson.deserializer.KeyDeserializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.syntifi.crypto.key.hash.Blake2b;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.util.ByteUtils;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Hex-encoded key, including the tag info.
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@JsonDeserialize(using = KeyDeserializer.class)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Key extends AbstractSerializedKeyTaggedHex<KeyTag> {

    public static Key deserialize(final DeserializerBuffer deser) throws NoSuchKeyTagException {
        try {
            final KeyTag keyTag = KeyTag.getByTag(deser.readU8());
            final Key key = keyTag.getKeyClass().getDeclaredConstructor().newInstance();
            key.setTag(keyTag);
            key.deserializeCustom(deser);
            return key;
        } catch (Exception e) {
            throw new NoSuchKeyTagException("Error deserializing key", e);
        }
    }

    protected void deserializeCustom(final DeserializerBuffer deser) throws Exception {
        this.setKey(deser.readByteArray(32));
    }

    public static Key fromTaggedHexString(final String hex) throws NoSuchKeyTagException {
        byte[] bytes = ByteUtils.parseHexString(hex);
        return deserialize(new DeserializerBuffer(bytes));
    }

    public String generateAccountHash(final boolean includePrefix) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(getTag().toString().toLowerCase().getBytes(StandardCharsets.UTF_8));
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(getKey());

        return (includePrefix ? "account-hash-" : "") + ByteUtils.encodeHexString(Blake2b.digest(byteArrayOutputStream.toByteArray(), 32));
    }

    @JsonCreator
    public void createKey(final String key) throws NoSuchKeyTagException {
        Key obj = Key.fromTaggedHexString(key);
        this.setTag(obj.getTag());
        this.setKey(obj.getKey());
    }
}
