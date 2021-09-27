package com.syntifi.casper.sdk.jackson.deserializer;

import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueString;

/**
 * Customize the mapping of Casper's String CLValue
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class CLValueStringDeserializer extends AbstractCLValueDeserializer<CLValueString> {

    @Override
    protected CLValueString getInstanceOf() {
        return new CLValueString();
    }
}
