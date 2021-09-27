package com.syntifi.casper.sdk.jackson.deserializer;

import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueByteArray;

/**
 * Customize the mapping of Casper's ByteArray CLValue
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see CLValueMap
 * @since 0.0.1
 */
public class CLValueByteArrayDeserializer extends AbstractCLValueDeserializer<CLValueByteArray> {

    @Override
    protected CLValueByteArray getInstanceOf() {
        return new CLValueByteArray();
    }
}



