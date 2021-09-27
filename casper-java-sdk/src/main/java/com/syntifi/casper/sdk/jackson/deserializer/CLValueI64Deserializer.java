package com.syntifi.casper.sdk.jackson.deserializer;

import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueI64;

/**
 * Customize the mapping of Casper's I64 CLValue
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class CLValueI64Deserializer extends AbstractCLValueDeserializer<CLValueI64> {

    @Override
    protected CLValueI64 getInstanceOf() {
        return new CLValueI64();
    }
}
