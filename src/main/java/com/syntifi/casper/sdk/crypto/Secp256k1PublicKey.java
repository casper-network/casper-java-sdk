package com.syntifi.casper.sdk.crypto;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
import org.web3j.crypto.Sign.SignatureData;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Secp256k1PublicKey extends PublicKey {

    public Secp256k1PublicKey(byte[] bytes) {
        super(bytes);
    }

    @Override
    public void readPublicKey(String filename) throws IOException {
        ASN1Primitive derKey = ASN1Primitive.fromByteArray(PemFileHelper.readPemFile(filename));
        ASN1Sequence objBaseSeq = ASN1Sequence.getInstance(derKey);
        String keyId = ASN1ObjectIdentifier
                .getInstance(ASN1Sequence.getInstance(objBaseSeq.getObjectAt(0)).getObjectAt(0)).getId();
        String curveId = ASN1ObjectIdentifier
                .getInstance(ASN1Sequence.getInstance(objBaseSeq.getObjectAt(0)).getObjectAt(1)).getId();
        if (curveId.equals(ASN1Identifiers.Secp256k1OIDCurve.getId())
                && keyId.equals(ASN1Identifiers.Secp256k1OIDkey.getId())) {
            DERBitString key = DERBitString.getInstance(objBaseSeq.getObjectAt(1));
            setKey(key.getBytes());
        } else {
            throw new IOException();
        }
    }

    @Override
    public void writePublicKey(String filename) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            //DERBitString key = new DERBitString(Secp256k1PublicKey.getShortKey(getKey()));
            DERBitString key = new DERBitString(getKey());
            ASN1EncodableVector v1 = new ASN1EncodableVector();
            v1.add(ASN1Identifiers.Secp256k1OIDkey);
            v1.add(ASN1Identifiers.Secp256k1OIDCurve);
            DERSequence derPrefix = new DERSequence(v1);
            ASN1EncodableVector v2 = new ASN1EncodableVector();
            v2.add(derPrefix);
            v2.add(key);
            DERSequence derKey = new DERSequence(v2);
            PemFileHelper.writePemFile(fileWriter, derKey.getEncoded(), ASN1Identifiers.PUBLIC_KEY_DER_HEADER);
        }
    }

    @Override
    public Boolean verify(String message, String signature) throws GeneralSecurityException {
        byte[] signatureBytes = Hex.decode(signature);
        SignatureData signatureData = new SignatureData(
                (byte) 27,
                Arrays.copyOfRange(signatureBytes, 0, 32),
                Arrays.copyOfRange(signatureBytes, 32, 64));
        BigInteger derivedKey = Sign.signedMessageHashToKey(Hash.sha256(message.getBytes()), signatureData);
        return Arrays.equals(Secp256k1PublicKey.getShortKey(derivedKey.toByteArray()), getKey());
    }

    public static byte[] getShortKey(byte[] key) {
        BigInteger pubKey = new BigInteger(key);
        String pubKeyPrefix = pubKey.testBit(0) ? "03" : "02";
        byte[] pubKeyBytes = Arrays.copyOf(key, 32);
        return Hex.decode(pubKeyPrefix + Hex.toHexString(pubKeyBytes));
    }
}
