package com.casper.sdk.model.clvalue.encdec.interfaces;

import com.casper.sdk.exception.CLValueEncodeException;
import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.exception.DynamicInstanceException;
import com.casper.sdk.model.clvalue.encdec.CLValueEncoder;

import java.io.IOException;

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
     * @param clve       the encoder to be used
     * @param encodeType append encoded type?
     * @throws NoSuchTypeException      thrown if type not found
     * @throws DynamicInstanceException thrown if it could not instantiate a type
     * @throws CLValueEncodeException   thrown if failed to encode a cl value
     * @throws IOException              thrown if an IO error occurs
     */
    void encode(CLValueEncoder clve, boolean encodeType)
            throws IOException, CLValueEncodeException, DynamicInstanceException, NoSuchTypeException;
}
