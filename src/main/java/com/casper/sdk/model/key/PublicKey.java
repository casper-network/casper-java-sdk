package com.casper.sdk.model.key;

import com.casper.sdk.jackson.deserializer.PublicKeyDeserializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.syntifi.crypto.key.AbstractPublicKey;
import com.syntifi.crypto.key.Ed25519PublicKey;
import com.syntifi.crypto.key.Secp256k1PublicKey;
import com.syntifi.crypto.key.hash.Blake2b;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.exception.ValueDeserializationException;
import dev.oak3.sbs4j.util.ByteUtils;
import lombok.NoArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
            throws NoSuchAlgorithmException, IllegalArgumentException {
        return PublicKey.fromBytes(ByteUtils.parseHexString(hex));
    }

    public static PublicKey fromBytes(final byte[] bytes) throws NoSuchAlgorithmException {
        final PublicKey object = new PublicKey();
        object.setTag(AlgorithmTag.getByTag(bytes[0]));
        object.setKey(Arrays.copyOfRange(bytes, 1, bytes.length));
        return object;
    }

    public static PublicKey fromAbstractPublicKey(final AbstractPublicKey key) {
        final PublicKey object = new PublicKey();
        object.setTag((key instanceof Secp256k1PublicKey) ? AlgorithmTag.SECP256K1 : AlgorithmTag.ED25519);
        object.setKey(key.getKey());
        return object;
    }

    public static PublicKey deserialize(final DeserializerBuffer deser) throws ValueDeserializationException, NoSuchAlgorithmException {
        // Obtain algorithm tag
        final AlgorithmTag tag = AlgorithmTag.getByTag(deser.readU8());
        final int len = tag.getLength();
        final PublicKey publicKey = new PublicKey();
        publicKey.setTag(tag);
        // Read the required number of bytes for the algorithm length
        publicKey.setKey(deser.readByteArray(len));
        return publicKey;
    }


    public String generateAccountHash(final boolean includePrefix) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(getTag().toString().toLowerCase().getBytes(StandardCharsets.UTF_8));
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(getKey());

        return (includePrefix ? "account-hash-" : "") + ByteUtils.encodeHexString(Blake2b.digest(byteArrayOutputStream.toByteArray(), 32));
    }

    @JsonCreator
    public void createPublicKey(final String key) throws NoSuchAlgorithmException, IllegalArgumentException {
        final PublicKey obj = PublicKey.fromTaggedHexString(key);
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
