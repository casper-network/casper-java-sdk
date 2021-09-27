package com.syntifi.casper.sdk.jackson.deserializer;

import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueU64;

/**
 * Customize the mapping of Casper's U64 CLValue
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class CLValueU64Deserializer extends AbstractCLValueDeserializer<CLValueU64> {

    @Override
    protected CLValueU64 getInstanceOf() {
        return new CLValueU64();
    }
}
