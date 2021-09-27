package com.syntifi.casper.sdk.jackson.deserializer;

import com.syntifi.casper.sdk.model.key.PublicKey;

/**
 * Customize the mapping of Casper's PublicKey
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class PublicKeyDeserializer extends AbstractAlgoTaggedHexDeserializer<PublicKey> {

    @Override
    protected PublicKey getInstanceOf() {
        return new PublicKey();
    }
}
