package com.syntifi.casper.sdk.jackson.deserializer;

import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueUnit;

/**
 * Customize the mapping of Casper's Unit CLValue
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class CLValueUnitDeserializer extends AbstractCLValueDeserializer<CLValueUnit> {

    @Override
    protected CLValueUnit getInstanceOf() {
        return new CLValueUnit();
    }
}

