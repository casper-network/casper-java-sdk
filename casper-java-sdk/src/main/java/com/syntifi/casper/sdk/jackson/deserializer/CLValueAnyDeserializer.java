package com.syntifi.casper.sdk.jackson.deserializer;

import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueAny;

/**
 * Customize the mapping of Casper's Any CLValue
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class CLValueAnyDeserializer extends AbstractCLValueDeserializer<CLValueAny> {

    @Override
    protected CLValueAny getInstanceOf() {
        return new CLValueAny();
    }
}
