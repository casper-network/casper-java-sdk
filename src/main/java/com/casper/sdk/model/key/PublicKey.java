package com.casper.sdk.model.key;

import com.casper.sdk.exception.InvalidByteStringException;
import com.casper.sdk.jackson.deserializer.PublicKeyDeserializer;
import com.casper.sdk.model.clvalue.encdec.StringByteHelper;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.syntifi.crypto.key.AbstractPublicKey;
import com.syntifi.crypto.key.Ed25519PublicKey;
import com.syntifi.crypto.key.Secp256k1PublicKey;
import lombok.NoArgsConstructor;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Hex-encoded cryptographic public key, including the algorithm tag prefix.
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@JsonDeserialize(using = PublicKeyDeserializer.class)
@NoArgsConstructor
public class PublicKey extends AbstractSerializedKeyTaggedHex<AlgorithmTag> {

    public static PublicKey fromTaggedHexString(String hex)
            throws NoSuchAlgorithmException, InvalidByteStringException {
        byte[] bytes = StringByteHelper.hexStringToByteArray(hex);
        return PublicKey.fromBytes(bytes);
    }

    public static PublicKey fromBytes(byte[] bytes) throws NoSuchAlgorithmException {
        PublicKey object = new PublicKey();
        object.setTag(AlgorithmTag.getByTag(bytes[0]));
        object.setKey(Arrays.copyOfRange(bytes, 1, bytes.length));

        return object;
    }

    public static PublicKey fromAbstractPublicKey(AbstractPublicKey key) {
        PublicKey object = new PublicKey();
        object.setTag((key instanceof Secp256k1PublicKey)
                ? AlgorithmTag.SECP256K1
                : AlgorithmTag.ED25519);
        object.setKey(key.getKey());
        return object;
    }


    @JsonCreator
    public void createPublicKey(String key) throws NoSuchAlgorithmException, InvalidByteStringException {
        PublicKey obj = PublicKey.fromTaggedHexString(key);
        this.setTag(obj.getTag());
        this.setKey(obj.getKey());
    }

    @JsonIgnore
    public AbstractPublicKey getPubKey() throws NoSuchAlgorithmException {
        if (getTag().equals(AlgorithmTag.ED25519)) {
            return new Ed25519PublicKey(getKey());
        } else if (getTag().equals(AlgorithmTag.SECP256K1)) {
            return new Secp256k1PublicKey(getKey());
        } else {
            throw new NoSuchAlgorithmException();
        }
    }
}
