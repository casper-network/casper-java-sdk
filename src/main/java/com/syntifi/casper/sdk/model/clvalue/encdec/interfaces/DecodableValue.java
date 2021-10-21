package com.syntifi.casper.sdk.model.clvalue.encdec.interfaces;

import java.io.IOException;

import com.syntifi.casper.sdk.exception.CLValueDecodeException;
import com.syntifi.casper.sdk.exception.DynamicInstanceException;
import com.syntifi.casper.sdk.exception.NoSuchTypeException;
import com.syntifi.casper.sdk.model.clvalue.encdec.CLValueDecoder;

/**
 * Defines an object as being capable of decoding with {@link CLValueDecoder}
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public interface DecodableValue {
    /**
     * Called when the object's values must be decoded after deserializing
     * 
     * @param clvd the decoder to be used
     * @throws IOException
     * @throws CLValueDecodeException
     * @throws DynamicInstanceException
     * @throws NoSuchTypeException
     */
    public void decode(CLValueDecoder clvd)
            throws IOException, CLValueDecodeException, DynamicInstanceException, NoSuchTypeException;
}
