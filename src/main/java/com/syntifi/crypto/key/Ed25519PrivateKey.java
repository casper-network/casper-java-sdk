package com.syntifi.crypto.key;

import com.syntifi.crypto.key.deterministic.HierarchicalDeterministicKey;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator;
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * ed25519 implementation of {@link AbstractPrivateKey}
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.1.0
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Ed25519PrivateKey extends AbstractPrivateKey {

    private Ed25519PrivateKeyParameters privateKeyParameters;

    public Ed25519PrivateKey(final byte[] privateKey) {
        super(privateKey);
        loadPrivateKey(privateKey);
    }

    @Override
    public void loadPrivateKey(final byte[] privateKey) {
        privateKeyParameters = new Ed25519PrivateKeyParameters(privateKey, 0);
    }

    /*
     * SEQUENCE (3 elem) INTEGER 0 SEQUENCE (1 elem) OBJECT IDENTIFIER 1.3.101.112
     * curveEd25519 (EdDSA 25519 signature algorithm) OCTET STRING (32 byte)
     * 38AECE974291F14B5FEF97E1B21F684394120B6E7A8AFB04398BBE787E8BC559 OCTET STRING
     * (32 byte) 38AECE974291F14B5FEF97E1B21F684394120B6E7A8AFB04398BBE787E8BC559
     */
    @Override
    public void readPrivateKey(final String filename) throws IOException {
        try (final Reader fileReader = new FileReader(filename)) {
            readPrivateKey(fileReader);
        }
    }

    @Override
    public void readPrivateKey(final Reader reader) throws IOException {
        final ASN1Primitive key = ASN1Primitive.fromByteArray(PemFileHelper.readPemFile(reader));
        final PrivateKeyInfo keyInfo = PrivateKeyInfo.getInstance(key);
        final String algoId = keyInfo.getPrivateKeyAlgorithm().getAlgorithm().toString();
        if (algoId.equals(ASN1Identifiers.Ed25519OID.getId())) {
            privateKeyParameters = new Ed25519PrivateKeyParameters(keyInfo.getPrivateKey().getEncoded(), 4);
            setKey(privateKeyParameters.getEncoded());
        }
    }

    @Override
    public void writePrivateKey(final String filename) throws IOException {
        try (final Writer fileWriter = new FileWriter(filename)) {
            writePrivateKey(fileWriter);
        }
    }

    @Override
    public void writePrivateKey(final Writer writer) throws IOException {
        final DERSequence derPrefix = new DERSequence(ASN1Identifiers.Ed25519OID);
        final DEROctetString key = new DEROctetString(new DEROctetString(getKey()));
        final ASN1EncodableVector vector = new ASN1EncodableVector();
        vector.add(new ASN1Integer(0));
        vector.add(derPrefix);
        vector.add(key);
        final DERSequence derKey = new DERSequence(vector);
        PemFileHelper.writePemFile(writer, derKey.getEncoded(), ASN1Identifiers.PRIVATE_KEY_DER_HEADER);
    }

    @Override
    public byte[] sign(final byte[] message) {
        final Signer signer = new Ed25519Signer();
        signer.init(true, privateKeyParameters);
        signer.update(message, 0, message.length);
        byte[] signature;
        try {
            signature = signer.generateSignature();
            return signature;
        } catch (DataLengthException | CryptoException e) {
            // TODO: throw new SomeException();
            return null;
        }
    }

    @Override
    public AbstractPublicKey derivePublicKey() {
        return new Ed25519PublicKey(privateKeyParameters.generatePublicKey().getEncoded());
    }

    public static Ed25519PrivateKey deriveFromSeed(final byte[] seed, final int[] path) throws IOException {
        final byte[] init = "ed25519 seed".getBytes(StandardCharsets.UTF_8);
        final byte[] key = HierarchicalDeterministicKey.getFromSeed(seed, init, path);
        return new Ed25519PrivateKey(key);
    }

    public static Ed25519PrivateKey deriveRandomKey() {
        final SecureRandom rnd = new SecureRandom();
        final Ed25519KeyPairGenerator keyPairGenerator = new Ed25519KeyPairGenerator();
        keyPairGenerator.init(new Ed25519KeyGenerationParameters(rnd));
        final AsymmetricCipherKeyPair asymmetricCipherKeyPair = keyPairGenerator.generateKeyPair();
        final Ed25519PrivateKeyParameters privateKeyParameters = (Ed25519PrivateKeyParameters) asymmetricCipherKeyPair.getPrivate();
        return new Ed25519PrivateKey(privateKeyParameters.getEncoded());
    }
}
