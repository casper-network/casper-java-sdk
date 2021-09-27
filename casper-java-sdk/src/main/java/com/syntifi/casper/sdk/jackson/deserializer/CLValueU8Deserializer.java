package com.syntifi.casper.sdk.jackson.deserializer;

import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueU8;

/**
 * Customize the mapping of Casper's U8 CLValue
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class CLValueU8Deserializer extends AbstractCLValueDeserializer<CLValueU8> {

    @Override
    protected CLValueU8 getInstanceOf() {
        return new CLValueU8();
    }
}
