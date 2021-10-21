package com.syntifi.casper.sdk.model.clvalue.encdec.interfaces;

import java.io.IOException;

import com.syntifi.casper.sdk.exception.CLValueEncodeException;
import com.syntifi.casper.sdk.exception.DynamicInstanceException;
import com.syntifi.casper.sdk.exception.NoSuchTypeException;
import com.syntifi.casper.sdk.model.clvalue.encdec.CLValueEncoder;

/**
 * Defines an object as being capable of encoding with {@link CLValueEncoder}
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public interface EncodableValue {
    /**
     * Called when the object's values must be encoded for serializing
     * 
     * @param clve the encoder to be used
     * @throws IOException
     * @throws CLValueEncodeException
     * @throws DynamicInstanceException
     * @throws NoSuchTypeException
     */
    public void encode(CLValueEncoder clve)
            throws IOException, CLValueEncodeException, DynamicInstanceException, NoSuchTypeException;
}
