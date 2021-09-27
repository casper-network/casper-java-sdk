package com.syntifi.casper.sdk.jackson.deserializer;

import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueMap;

/**
 * Customize the mapping of Casper's Map CLValue
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see CLValueMap
 * @since 0.0.1
 */
public class CLValueMapDeserializer extends AbstractCLValueDeserializer<CLValueMap> {

    @Override
    protected CLValueMap getInstanceOf() {
        return new CLValueMap();
    }
}



