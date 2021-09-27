package com.syntifi.casper.sdk.jackson.deserializer;

import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueI32;

/**
 * Customize the mapping of Casper's I32 CLValue
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class CLValueI32Deserializer extends AbstractCLValueDeserializer<CLValueI32> {

    @Override
    protected CLValueI32 getInstanceOf() {
        return new CLValueI32();
    }
}
