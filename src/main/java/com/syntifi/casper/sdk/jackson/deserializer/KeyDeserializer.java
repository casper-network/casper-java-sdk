package com.syntifi.casper.sdk.jackson.deserializer;

import com.syntifi.casper.sdk.exception.NoSuchKeyTagException;
import com.syntifi.casper.sdk.model.key.Key;
import com.syntifi.casper.sdk.model.key.KeyTag;

import java.util.Arrays;

/**
 * Customize the mapping of Casper's PublicKey
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class KeyDeserializer extends AbstractSerializedKeyTaggedHexDeserializer<Key, KeyTag> {

    @Override
    protected Key getInstanceOf() {
        return new Key();
    }

    @Override
    protected void loadKey(Key key, byte[] bytes) throws NoSuchKeyTagException {
        key.setTag(KeyTag.getByTag(bytes[0]));
        key.setKey(Arrays.copyOfRange(bytes, 1, bytes.length));
    }
}
