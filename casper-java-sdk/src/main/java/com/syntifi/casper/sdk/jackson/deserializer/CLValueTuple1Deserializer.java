package com.syntifi.casper.sdk.jackson.deserializer;

import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueTuple1;

/**
 * Customize the mapping of Casper's Tuple1 CLValue
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class CLValueTuple1Deserializer extends AbstractCLValueDeserializer<CLValueTuple1> {

    @Override
    protected CLValueTuple1 getInstanceOf() {
        return new CLValueTuple1();
    }
}

