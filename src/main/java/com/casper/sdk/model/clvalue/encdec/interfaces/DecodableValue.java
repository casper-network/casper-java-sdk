package com.casper.sdk.model.clvalue.encdec.interfaces;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.exception.CLValueDecodeException;
import com.casper.sdk.exception.DynamicInstanceException;
import com.casper.sdk.model.clvalue.encdec.CLValueDecoder;

import java.io.IOException;

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
     * @throws NoSuchTypeException      thrown if type not found
     * @throws DynamicInstanceException thrown if it could not instantiate a type
     * @throws CLValueDecodeException   thrown if failed to decode a cl value
     * @throws IOException              thrown if an IO error occurs
     */
    void decode(CLValueDecoder clvd)
            throws IOException, CLValueDecodeException, DynamicInstanceException, NoSuchTypeException;
}
