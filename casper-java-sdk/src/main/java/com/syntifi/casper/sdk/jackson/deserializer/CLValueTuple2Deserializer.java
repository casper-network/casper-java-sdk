package com.syntifi.casper.sdk.jackson.deserializer;

import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueTuple2;

/**
 * Customize the mapping of Casper's Tuple2 CLValue
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class CLValueTuple2Deserializer extends AbstractCLValueDeserializer<CLValueTuple2> {

    @Override
    protected CLValueTuple2 getInstanceOf() {
        return new CLValueTuple2();
    }
}

