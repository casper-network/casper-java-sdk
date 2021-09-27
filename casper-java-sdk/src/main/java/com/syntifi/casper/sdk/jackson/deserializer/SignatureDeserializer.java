package com.syntifi.casper.sdk.jackson.deserializer;

import com.syntifi.casper.sdk.model.key.Signature;

/**
 * Customize the mapping of Casper's Signature
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class SignatureDeserializer extends AbstractAlgoTaggedHexDeserializer<Signature> {

    @Override
    protected Signature getInstanceOf() {
        return new Signature();
    }
}
