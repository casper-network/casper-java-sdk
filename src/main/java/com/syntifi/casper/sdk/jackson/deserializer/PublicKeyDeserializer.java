package com.syntifi.casper.sdk.jackson.deserializer;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import com.syntifi.casper.sdk.exception.NoSuchKeyTagException;
import com.syntifi.casper.sdk.model.key.AlgorithmTag;
import com.syntifi.casper.sdk.model.key.PublicKey;

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
    protected void loadKey(PublicKey key, byte[] bytes) throws NoSuchAlgorithmException, NoSuchKeyTagException {
        key.setTag(AlgorithmTag.getByTag(bytes[0]));
        key.setKey(Arrays.copyOfRange(bytes, 1, bytes.length));
    }
}
