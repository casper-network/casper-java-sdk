package com.syntifi.casper.sdk.model.key;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.syntifi.casper.sdk.jackson.deserializer.SignatureDeserializer;

import lombok.NoArgsConstructor;

/**
 * Hex-encoded cryptographic public key, including the algorithm tag prefix.
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@JsonDeserialize(using = SignatureDeserializer.class)
@NoArgsConstructor
public class Signature extends AbstractSerializedKeyTaggedHex<AlgorithmTag> {
}
