package com.syntifi.casper.sdk.jackson.deserializer;

import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueList;

/**
 * Customize the mapping of Casper's List CLValue
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see CLValueList
 * @since 0.0.1
 */
public class CLValueListDeserializer extends AbstractCLValueDeserializer<CLValueList> {

    @Override
    protected CLValueList getInstanceOf() {
        return new CLValueList();
    }
}


