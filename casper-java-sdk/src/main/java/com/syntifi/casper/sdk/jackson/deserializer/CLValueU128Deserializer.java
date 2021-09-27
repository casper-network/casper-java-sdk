package com.syntifi.casper.sdk.jackson.deserializer;

import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueU128;

/**
 * Customize the mapping of Casper's U128 CLValue
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class CLValueU128Deserializer extends AbstractCLValueDeserializer<CLValueU128> {

    @Override
    protected CLValueU128 getInstanceOf() {
        return new CLValueU128();
    }
}
