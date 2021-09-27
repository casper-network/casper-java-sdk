package com.syntifi.casper.sdk.jackson.deserializer;

import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueTuple3;

/**
 * Customize the mapping of Casper's Tuple3 CLValue
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class CLValueTuple3Deserializer extends AbstractCLValueDeserializer<CLValueTuple3> {

    @Override
    protected CLValueTuple3 getInstanceOf() {
        return new CLValueTuple3();
    }
}

