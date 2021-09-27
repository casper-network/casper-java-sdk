package com.syntifi.casper.sdk.jackson.deserializer;

import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueOption;

/**
 * Customize the mapping of Casper's Option CLValue
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class CLValueOptionDeserializer extends AbstractCLValueDeserializer<CLValueOption> {

    @Override
    protected CLValueOption getInstanceOf() {
        return new CLValueOption();
    }
}
