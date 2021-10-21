package com.syntifi.casper.sdk.model.key;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.syntifi.casper.sdk.exception.InvalidByteStringException;
import com.syntifi.casper.sdk.exception.NoSuchKeyTagException;
import com.syntifi.casper.sdk.jackson.deserializer.KeyDeserializer;
import com.syntifi.casper.sdk.model.clvalue.encdec.StringByteHelper;

import lombok.NoArgsConstructor;

/**
 * Hex-encoded key, including the tag info.
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@JsonDeserialize(using = KeyDeserializer.class)
@NoArgsConstructor
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
