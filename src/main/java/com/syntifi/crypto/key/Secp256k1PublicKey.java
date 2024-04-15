package com.syntifi.crypto.key;

import com.syntifi.crypto.key.encdec.Hex;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.bouncycastle.asn1.*;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
import org.web3j.crypto.Sign.SignatureData;

import java.io.*;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.Arrays;

/**
 * secp256k1 implementation of {@link AbstractPublicKey}
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.1.0
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Secp256k1PublicKey extends AbstractPublicKey {

    public Secp256k1PublicKey(final byte[] bytes) {
        super(bytes);
    }

    @Override
    public void loadPublicKey(final byte[] publicKey) throws IOException {
        final ASN1Primitive derKey = ASN1Primitive.fromByteArray(publicKey);
        final ASN1Sequence objBaseSeq = ASN1Sequence.getInstance(derKey);
        final String keyId = ASN1ObjectIdentifier.getInstance(ASN1Sequence.getInstance(objBaseSeq.getObjectAt(0)).getObjectAt(0)).getId();
        final String curveId = ASN1ObjectIdentifier.getInstance(ASN1Sequence.getInstance(objBaseSeq.getObjectAt(0)).getObjectAt(1)).getId();
        if (curveId.equals(ASN1Identifiers.Secp256k1OIDCurve.getId())
                && keyId.equals(ASN1Identifiers.Secp256k1OIDkey.getId())) {
            final DERBitString key = DERBitString.getInstance(objBaseSeq.getObjectAt(1));
            setKey(key.getBytes());
        } else {
            throw new IOException();
        }

    }

    @Override
    public void readPublicKey(final String filename) throws IOException {
        try (final Reader fileReader = new FileReader(filename)) {
            readPublicKey(fileReader);
        }
    }

    @Override
    public void readPublicKey(final Reader reader) throws IOException {
        loadPublicKey(PemFileHelper.readPemFile(reader));
    }

    @Override
    public void writePublicKey(final String filename) throws IOException {
        try (final FileWriter fileWriter = new FileWriter(filename)) {
            writePublicKey(fileWriter);
        }
    }

    @Override
    public void writePublicKey(final Writer writer) throws IOException {
        final DERBitString key = new DERBitString(getKey());
        final ASN1EncodableVector v1 = new ASN1EncodableVector();
        v1.add(ASN1Identifiers.Secp256k1OIDkey);
        v1.add(ASN1Identifiers.Secp256k1OIDCurve);
        final DERSequence derPrefix = new DERSequence(v1);
        final ASN1EncodableVector v2 = new ASN1EncodableVector();
        v2.add(derPrefix);
        v2.add(key);
        final DERSequence derKey = new DERSequence(v2);
        PemFileHelper.writePemFile(writer, derKey.getEncoded(), ASN1Identifiers.PUBLIC_KEY_DER_HEADER);
    }

    @Override
    public Boolean verify(final byte[] message, final byte[] signature) throws GeneralSecurityException {
        //TODO: Double check the issue the getV(), for now we are trying with both (27 and 28)
        final SignatureData signatureData1 = new SignatureData(
                (byte) 27,
                Arrays.copyOfRange(signature, 0, 32),
                Arrays.copyOfRange(signature, 32, 64));
        final BigInteger derivedKey1 = Sign.signedMessageHashToKey(Hash.sha256(message), signatureData1);
        final SignatureData signatureData2 = new SignatureData(
                (byte) 28,
                Arrays.copyOfRange(signature, 0, 32),
                Arrays.copyOfRange(signature, 32, 64));
        final BigInteger derivedKey2 = Sign.signedMessageHashToKey(Hash.sha256(message), signatureData2);
        return Arrays.equals(Secp256k1PublicKey.getShortKey(derivedKey1.toByteArray()), getKey()) ||
                Arrays.equals(Secp256k1PublicKey.getShortKey(derivedKey2.toByteArray()), getKey());
    }

    /**
     * Gets a short key
     *
     * @param key the key as a byte array
     * @return short key as byte array
     */
    public static byte[] getShortKey(final byte[] key) {
        final BigInteger pubKey = new BigInteger(key);
        final String pubKeyPrefix = pubKey.testBit(0) ? "03" : "02";
        final byte[] pubKeyBytes = Arrays.copyOfRange(key, 0, 32);
        return Hex.decode(pubKeyPrefix + Hex.encode(pubKeyBytes));
    }
}
