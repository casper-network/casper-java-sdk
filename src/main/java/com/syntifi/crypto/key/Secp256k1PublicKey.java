package com.syntifi.crypto.key;

import com.casper.sdk.model.key.AlgorithmTag;
import com.syntifi.crypto.key.encdec.Hex;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.bouncycastle.asn1.*;
import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.math.BigInteger;
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
    public void readPublicKey(final Reader reader) throws IOException {
        loadPublicKey(PemFileHelper.readPemFile(reader));
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

    /**
     * Iterates possible signature combinations and possible recovery id's
     * Casper does not use signature.v so we have to iterate v
     * We don't know v so we have to iterate the possible recover id's
     * Converts to short public key for comparison
     *
     * @param message   the signed message
     * @param signature the signature to check against
     * @return true|false public key found
     */
    @Override
    public Boolean verify(byte[] message, byte[] signature) {

        //We need the Public key's short key
        byte[] keyToFind = (getKey().length > AlgorithmTag.SECP256K1.getLength()) ? getShortKey(getKey()) : getKey();

        //Looping possible v's of the signature
        for (int i = 27; i <= 34; i++) {

            final Sign.SignatureData signatureData =
                    new Sign.SignatureData(
                            (byte) (i),
                            Arrays.copyOfRange(signature, 0, 32),
                            Arrays.copyOfRange(signature, 32, 64));

            //iterate the recovery id's
            for (int j = 0; j < 4; j++) {

                final ECDSASignature ecdsaSignature = new ECDSASignature(new BigInteger(1, signatureData.getR()),
                        new BigInteger(1, signatureData.getS()));
                final BigInteger recoveredKey = Sign.recoverFromSignature((byte) j, ecdsaSignature, Hash.sha256(message));

                if (recoveredKey != null) {

                    final byte[] keyFromSignature = getShortKey(recoveredKey.toByteArray());

                    if (Arrays.equals(keyFromSignature, keyToFind)) {
                        return true;
                    }
                }
            }

        }

        return false;

    }

    /**
     * Gets a short key
     * There's around a 50% chance the elliptical curve algo will generate a 65 byte
     * public key instead of 66 byte.
     * Luckily the algo pads the first byte as zero when this happens
     * startBit determines this
     *
     * @param key the key as a byte array
     * @return short key as byte array
     */
    public static byte[] getShortKey(final byte[] key) {
        final BigInteger pubKey = new BigInteger(key);
        final String pubKeyPrefix = pubKey.testBit(0) ? "03" : "02";

        final int startBit = key[0] == (byte) 0 ? 1 : 0;

        final byte[] pubKeyBytes = Arrays.copyOfRange(key, startBit, (AlgorithmTag.SECP256K1.getLength() - 1) + startBit);
        return Hex.decode(pubKeyPrefix + Hex.encode(pubKeyBytes));
    }

}
