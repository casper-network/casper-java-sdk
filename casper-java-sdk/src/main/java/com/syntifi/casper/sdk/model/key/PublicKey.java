package com.syntifi.casper.sdk.model.key;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.syntifi.casper.sdk.jackson.deserializer.PublicKeyDeserializer;
import com.syntifi.casper.sdk.jackson.serializer.AbstractAlgoTaggedHexSerializer;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import lombok.NoArgsConstructor;

/**
 * Hex-encoded cryptographic public key, including the algorithm tag prefix.
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@JsonSerialize(using = AbstractAlgoTaggedHexSerializer.class)
@JsonDeserialize(using = PublicKeyDeserializer.class)
@NoArgsConstructor
public class PublicKey extends AlgoTaggedHex {

    public static PublicKey fromTaggedHexString(String hex) throws DecoderException, NoSuchAlgorithmException{
        PublicKey object = new PublicKey();
        byte[] bytes = Hex.decodeHex(hex.toCharArray());
        object.setAlgorithm(Algorithm.getByTag(bytes[0]));
        object.setKey(Arrays.copyOfRange(bytes, 1, bytes.length));
        return object;
    }

}
