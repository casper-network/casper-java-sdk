package com.syntifi.casper.sdk.jackson.deserializer;

import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueU512;

/**
 * Customize the mapping of Casper's U512 CLValue
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class CLValueU512Deserializer extends AbstractCLValueDeserializer<CLValueU512> {

    @Override
    protected CLValueU512 getInstanceOf() {
        return new CLValueU512();
    }
}
