package com.syntifi.casper.sdk.crypto;

import java.io.IOException;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;

public class Ed25519CryptoService {

    private Ed25519PrivateKeyParameters ed25519PrivateKey;
    private Ed25519PublicKeyParameters ed25519PublicKey;
    // 1.3.101.112 is the OID identifier for the Ed25519 schema
    private static final ASN1ObjectIdentifier OID = new ASN1ObjectIdentifier("1.3.101.112");

    /*
     * SEQUENCE (3 elem) INTEGER 0 SEQUENCE (1 elem) OBJECT IDENTIFIER 1.3.101.112
     * curveEd25519 (EdDSA 25519 signature algorithm) OCTET STRING (32 byte)
     * 38AECE974291F14B5FEF97E1B21F684394120B6E7A8AFB04398BBE787E8BC559 OCTET STRING
     * (32 byte) 38AECE974291F14B5FEF97E1B21F684394120B6E7A8AFB04398BBE787E8BC559
     */
    public void readPrivateKey(String filename) throws IOException {
        ASN1Primitive key = ASN1Primitive.fromByteArray(PemFileHelper.readPemFile(filename));
        PrivateKeyInfo keyInfo = PrivateKeyInfo.getInstance(key);
        String algoId = keyInfo.getPrivateKeyAlgorithm().getAlgorithm().toString();
        if (algoId.equals(OID.getId())) {
            ed25519PrivateKey = new Ed25519PrivateKeyParameters(keyInfo.getPrivateKey().getEncoded(), 4);
        }
    }

    public void writePrivateKey(String filename) throws IOException {
        DERSequence derPrefix = new DERSequence(OID);
        DEROctetString key = new DEROctetString(new DEROctetString(ed25519PrivateKey.getEncoded()));
        ASN1EncodableVector vector = new ASN1EncodableVector();
        vector.add(new ASN1Integer(0));
        vector.add(derPrefix);
        vector.add(key);
        DERSequence derKey = new DERSequence(vector);
        PemFileHelper.writePemFile(filename, derKey.getEncoded(), "PRIVATE KEY");
    }

    /*
     * SEQUENCE (2 elem) SEQUENCE (1 elem) OBJECT IDENTIFIER 1.3.101.112
     * curveEd25519 (EdDSA 25519 signature algorithm) BIT STRING (256 bit) <KEY
     * HERE>
     */
    public void readPublicKey(String filename) throws IOException {
        ASN1Primitive derKey = ASN1Primitive.fromByteArray(PemFileHelper.readPemFile(filename));
        ASN1Sequence objBaseSeq = ASN1Sequence.getInstance(derKey);
        String objId = ASN1ObjectIdentifier
                .getInstance(ASN1Sequence.getInstance(objBaseSeq.getObjectAt(0)).getObjectAt(0)).getId();
        if (objId.equals(OID.getId())) {
            DERBitString key = DERBitString.getInstance(objBaseSeq.getObjectAt(1));
            ed25519PublicKey = new Ed25519PublicKeyParameters(key.getBytes(), 0);
        }

        //setPublicKey(Hex.toHexString(ed25519PublicKey.getEncoded()));
    }

    public void writePublicKey(String filename) throws IOException {
        DERSequence derPrefix = new DERSequence(OID);
        DERBitString key = new DERBitString(ed25519PublicKey.getEncoded());
        ASN1EncodableVector vector = new ASN1EncodableVector();
        vector.add(derPrefix);
        vector.add(key);
        DERSequence derKey = new DERSequence(vector);
        PemFileHelper.writePemFile(filename, derKey.getEncoded(), "PUBLIC KEY");
    }

    public Ed25519PublicKeyParameters derivePublicKey() {
        return ed25519PrivateKey.generatePublicKey();
    }

    public static Ed25519CryptoService fromPublicKeyBytes(byte[] bytes) {
        return new Ed25519CryptoService();
    }

    public String sign(String msg) {
        return msg;
    }
}
