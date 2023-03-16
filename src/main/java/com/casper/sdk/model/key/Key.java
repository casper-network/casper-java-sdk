package com.casper.sdk.model.key;

import com.casper.sdk.exception.NoSuchKeyTagException;
import com.casper.sdk.jackson.deserializer.KeyDeserializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.syntifi.crypto.key.hash.Blake2b;
import dev.oak3.sbs4j.util.ByteUtils;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

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

    public static Key fromTaggedHexString(String hex) throws NoSuchKeyTagException {
        Key object = new Key();
        byte[] bytes = ByteUtils.parseHexString(hex);
        object.setTag(KeyTag.getByTag(bytes[0]));
        object.setKey(Arrays.copyOfRange(bytes, 1, bytes.length));
        return object;
    }

    public String generateAccountHash(boolean includePrefix) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(getTag().toString().toLowerCase().getBytes("UTF-8"));
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(getKey());

        return (includePrefix ? "account-hash-" : "") + ByteUtils.encodeHexString(Blake2b.digest(byteArrayOutputStream.toByteArray(), 32));
    }

    @JsonCreator
    public void createKey(String key) throws NoSuchKeyTagException {
        Key obj = Key.fromTaggedHexString(key);
        this.setTag(obj.getTag());
        this.setKey(obj.getKey());
    }
}
