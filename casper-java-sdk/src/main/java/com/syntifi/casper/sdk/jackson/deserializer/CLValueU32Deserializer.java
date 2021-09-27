package com.syntifi.casper.sdk.jackson.deserializer;

import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueU32;

/**
 * Customize the mapping of Casper's U32 CLValue
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class CLValueU32Deserializer extends AbstractCLValueDeserializer<CLValueU32> {

    @Override
    protected CLValueU32 getInstanceOf() {
        return new CLValueU32();
    }
}
