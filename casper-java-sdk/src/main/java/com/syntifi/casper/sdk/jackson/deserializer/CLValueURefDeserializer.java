package com.syntifi.casper.sdk.jackson.deserializer;

import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueURef;

/**
 * Customize the mapping of Casper's URef CLValue
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class CLValueURefDeserializer extends AbstractCLValueDeserializer<CLValueURef> {

    @Override
    protected CLValueURef getInstanceOf() {
        return new CLValueURef();
    }
}

