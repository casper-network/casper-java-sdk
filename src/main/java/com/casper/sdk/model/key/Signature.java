package com.casper.sdk.model.key;

import com.casper.sdk.jackson.deserializer.SignatureDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.syntifi.crypto.key.AbstractPrivateKey;
import com.syntifi.crypto.key.Secp256k1PrivateKey;
import com.syntifi.crypto.key.encdec.Hex;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

/**
 * Hex-encoded cryptographic public key, including the algorithm tag prefix.
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@JsonDeserialize(using = SignatureDeserializer.class)
@NoArgsConstructor
@Builder
public class Signature extends AbstractSerializedKeyTaggedHex<AlgorithmTag> {

    public static Signature sign(AbstractPrivateKey key, byte[] msg) throws GeneralSecurityException {
        byte[] signatureBytes = key.sign(msg);
        Signature signature = new Signature();
        signature.setKey(signatureBytes);
        signature.setTag((key instanceof Secp256k1PrivateKey)
                ? AlgorithmTag.SECP256K1
                : AlgorithmTag.ED25519);
        return signature;
    }

    public static Signature fromHex(final String hex) throws NoSuchAlgorithmException {
        final Signature signature = new Signature();
        byte[] tag = Hex.decode(hex.substring(0, 2));
        byte[] sig = Hex.decode(hex.substring(2));
        signature.setTag(AlgorithmTag.getByTag(tag[0]));
        signature.setKey(sig);
        return signature;
    }
}
