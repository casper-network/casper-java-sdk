package com.syntifi.casper.sdk.model.key;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.syntifi.casper.sdk.jackson.deserializer.SignatureDeserializer;
import com.syntifi.casper.sdk.jackson.serializer.AbstractAlgoTaggedHexSerializer;

import lombok.NoArgsConstructor;

/**
 * Hex-encoded cryptographic public key, including the algorithm tag prefix.
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@JsonSerialize(using = AbstractAlgoTaggedHexSerializer.class)
@JsonDeserialize(using = SignatureDeserializer.class)
@NoArgsConstructor
public class Signature extends AlgoTaggedHex {

}
