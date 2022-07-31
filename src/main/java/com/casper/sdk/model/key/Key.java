package com.casper.sdk.model.key;

import com.casper.sdk.exception.InvalidByteStringException;
import com.casper.sdk.exception.NoSuchKeyTagException;
import com.casper.sdk.jackson.deserializer.KeyDeserializer;
import com.casper.sdk.model.clvalue.encdec.StringByteHelper;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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

    public static Key fromTaggedHexString(String hex) throws NoSuchKeyTagException, InvalidByteStringException {
        Key object = new Key();
        byte[] bytes = StringByteHelper.hexStringToByteArray(hex);
        object.setTag(KeyTag.getByTag(bytes[0]));
        object.setKey(Arrays.copyOfRange(bytes, 1, bytes.length));
        return object;
    }

    @JsonCreator
    public void createKey(String key) throws NoSuchKeyTagException, InvalidByteStringException {
        Key obj = Key.fromTaggedHexString(key);
        this.setTag(obj.getTag());
        this.setKey(obj.getKey());
    }
}
