package com.syntifi.casper.sdk.crypto;

import java.io.IOException;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;

import lombok.Data;

public class Secp256k1CryptoService {
    private static final ASN1ObjectIdentifier OIDCurve = new ASN1ObjectIdentifier("1.3.132.0.10");
    private static final ASN1ObjectIdentifier OIDkey = new ASN1ObjectIdentifier("1.2.840.10045.2.1");
    private ECKeyPair keyPair;

    public void readPrivateKey(String filename) throws IOException {
        ASN1Sequence key = ASN1Sequence.getInstance(PemFileHelper.readPemFile(filename));
        PrivateKeyInfo keyInfo = PrivateKeyInfo.getInstance(key.getObjectAt(1));
        String algoId = keyInfo.getPrivateKeyAlgorithm().getAlgorithm().toString();
        if (algoId.equals(OIDCurve.getId())) {
            Credentials cs = Credentials.create(Hex.toHexString(keyInfo.getEncoded()));
            keyPair = cs.getEcKeyPair();
        }
    }

    public String derivePublicKey() {
        return keyPair.getPublicKey().toString(16);
    }

    /*
     * SEQUENCE (2 elem) SEQUENCE (2 elem) OBJECT IDENTIFIER 1.2.840.10045.2.1
     * ecPublicKey (ANSI X9.62 public key type) OBJECT IDENTIFIER 1.3.132.0.10
     * secp256k1 (SECG (Certicom) named elliptic curve) BIT STRING (264 bit) <KEY
     * HERE>
     */
    public void readPublicKey(String filename) throws IOException {
        ASN1Primitive derKey = ASN1Primitive.fromByteArray(PemFileHelper.readPemFile(filename));
        ASN1Sequence objBaseSeq = ASN1Sequence.getInstance(derKey);
        String keyId = ASN1ObjectIdentifier
                .getInstance(ASN1Sequence.getInstance(objBaseSeq.getObjectAt(0)).getObjectAt(0)).getId();
        String curveId = ASN1ObjectIdentifier
                .getInstance(ASN1Sequence.getInstance(objBaseSeq.getObjectAt(0)).getObjectAt(1)).getId();
        if (curveId.equals(OIDCurve.getId()) && keyId.equals(OIDkey.getId())) {
            DERBitString key = DERBitString.getInstance(objBaseSeq.getObjectAt(1));
            //setPublicKey(Hex.toHexString(key.getBytes()));
        }
    }

    public String sign(String msg) {
        return msg;
    }

    public Secp256k1CryptoService fromPublicKeyBytes(byte[] bytes) {
        return new Secp256k1CryptoService();
    }
}
