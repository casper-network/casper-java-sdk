package com.syntifi.casper.sdk.jackson.deserializer;

import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValuePublicKey;

/**
 * Customize the mapping of Casper's PublicKey CLValue
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class CLValuePublicKeyDeserializer extends AbstractCLValueDeserializer<CLValuePublicKey> {

    @Override
    protected CLValuePublicKey getInstanceOf() {
        return new CLValuePublicKey();
    }    
}
