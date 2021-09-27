package com.syntifi.casper.sdk.jackson.deserializer;

import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueBool;

/**
 * Customize the mapping of Casper's Bool CLValue
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class CLValueBoolDeserializer extends AbstractCLValueDeserializer<CLValueBool> {

    @Override
    protected CLValueBool getInstanceOf() {
        return new CLValueBool();
    }
}
