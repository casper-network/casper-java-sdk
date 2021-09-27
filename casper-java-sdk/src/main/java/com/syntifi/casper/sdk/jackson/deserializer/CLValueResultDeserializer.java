package com.syntifi.casper.sdk.jackson.deserializer;

import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueResult;

/**
 * Customize the mapping of Casper's Result CLValue
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see CLValueMap
 * @since 0.0.1
 */
public class CLValueResultDeserializer extends AbstractCLValueDeserializer<CLValueResult> {

    @Override
    protected CLValueResult getInstanceOf() {
        return new CLValueResult();
    }
}



