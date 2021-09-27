package com.syntifi.casper.sdk.jackson.deserializer;

import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueU256;

/**
 * Customize the mapping of Casper's U256 CLValue
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class CLValueU256Deserializer extends AbstractCLValueDeserializer<CLValueU256> {

    @Override
    protected CLValueU256 getInstanceOf() {
        return new CLValueU256();
    }
}
