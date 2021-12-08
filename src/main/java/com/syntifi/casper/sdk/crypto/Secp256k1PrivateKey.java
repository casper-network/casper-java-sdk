package com.syntifi.casper.sdk.crypto;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
import org.web3j.crypto.Sign.SignatureData;

import lombok.Getter;

@Getter
public class Secp256k1PrivateKey extends AbstractPrivateKey {

    private ECKeyPair keyPair;

    @Override
    public void readPrivateKey(String filename) throws IOException {
        ASN1Sequence key = (ASN1Sequence) ASN1Primitive.fromByteArray(PemFileHelper.readPemFile(filename));
        String algoId = key.getObjectAt(2).toString();
        if (algoId.equals("[0]" + ASN1Identifiers.Secp256k1OIDCurve) && key.getObjectAt(0).toString().equals("1")) {
            DEROctetString pk = (DEROctetString) key.getObjectAt(1);
            keyPair = ECKeyPair.create(pk.getOctets());
            this.setKey(keyPair.getPrivateKey().toByteArray());
        }
    }

    @Override
    public void writePrivateKey(String filename) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            DERTaggedObject derPrefix = new DERTaggedObject(0, ASN1Identifiers.Secp256k1OIDCurve);
            DEROctetString key = new DEROctetString(getKey());
            ASN1EncodableVector vector = new ASN1EncodableVector();
            vector.add(new ASN1Integer(1));
            vector.add(key);
            vector.add(derPrefix);
            DERSequence derKey = new DERSequence(vector);
            PemFileHelper.writePemFile(fileWriter, derKey.getEncoded(), ASN1Identifiers.EC_PRIVATE_KEY_DER_HEADER);
        }
    }

    /**
     * When encoded in DER, this becomes the following sequence of bytes:
     * 
     * 0x30 b1 0x02 b2 (vr) 0x02 b3 (vs)
     * 
     * where:
     * 
     * b1 is a single byte value, equal to the length, in bytes, of the remaining
     * list of bytes (from the first 0x02 to the end of the encoding);
     * b2 is a single byte value, equal to the length, in bytes, of (vr);
     * b3 is a single byte value, equal to the length, in bytes, of (vs);
     * (vr) is the signed big-endian encoding of the value "r
     * 
     * ", of minimal length;
     * (vs) is the signed big-endian encoding of the value "s
     * ", of minimal length.
     */
    @Override
    public String sign(String message) {
        SignatureData signature = Sign.signMessage(Hash.sha256(message.getBytes()), keyPair, false);
        return Hex.toHexString(signature.getR()) + Hex.toHexString(signature.getS());
        // return Hex.toHexString(signature.getR()) + canonical(signature.getS());
        // return r.toString(16) + Sign.CURVE_PARAMS.getN().subtract(s).toString(16);
    }

    /**
     * Will automatically adjust the S component to be more than or equal to half
     * the curve order,
     * if necessary. This is required because for every signature (r,s) the
     * signature (r, -s (mod
     * N)) is a valid signature of the same message. However, we dislike the ability
     * to modify the
     * bits of a Ethereum transaction after it's been signed, as that violates
     * various assumed
     * invariants. Thus in future only one of those forms will be considered legal
     * and the other
     * will be banned.
     *
     */
    private String canonical(byte[] s) {
        BigInteger sBigInteger = new BigInteger(s);
        BigInteger N = Sign.CURVE_PARAMS.getN();
        BigInteger HALF_N = N.shiftRight(1);
        return sBigInteger.compareTo(HALF_N) < 0 ? N.subtract(sBigInteger).toString(16) : Hex.toHexString(s);
    }

    /**
     * Returns a Secp256k1PublicKey object in a compressed format
     * adding the prefix 02/03 to identify the positive or negative Y followed
     * by the X value in the elliptic curve
     */
    @Override
    public AbstractPublicKey derivePublicKey() {
        //return new Secp256k1PublicKey(keyPair.getPublicKey().toByteArray());
        BigInteger pubKey = keyPair.getPublicKey();
        String pubKeyPrefix = pubKey.testBit(0) ? "03" : "02";
        byte[] pubKeyBytes = Arrays.copyOf(pubKey.toByteArray(), 32);
        return new Secp256k1PublicKey(Hex.decode(pubKeyPrefix + Hex.toHexString(pubKeyBytes)));
    }
}
