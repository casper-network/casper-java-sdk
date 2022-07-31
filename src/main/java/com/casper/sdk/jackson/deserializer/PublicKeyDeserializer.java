package com.casper.sdk.jackson.deserializer;

import com.casper.sdk.model.key.AlgorithmTag;
import com.casper.sdk.model.key.PublicKey;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Customize the mapping of Casper's PublicKey
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class PublicKeyDeserializer extends AbstractSerializedKeyTaggedHexDeserializer<PublicKey, AlgorithmTag> {

    @Override
    protected PublicKey getInstanceOf() {
        return new PublicKey();
    }

    @Override
    protected void loadKey(PublicKey key, byte[] bytes) throws NoSuchAlgorithmException {
        key.setTag(AlgorithmTag.getByTag(bytes[0]));
        key.setKey(Arrays.copyOfRange(bytes, 1, bytes.length));
    }
}
