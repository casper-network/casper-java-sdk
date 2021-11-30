package com.syntifi.casper.sdk.crypto;

import java.io.IOException;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import org.bouncycastle.util.encoders.Hex;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Ed25519PrivateKey extends PrivateKey {
    public static final String PRIVATE_KEY_DER_HEADER = "PRIVATE KEY";

    // 1.3.101.112 is the OID identifier for the Ed25519 schema
    private static final ASN1ObjectIdentifier OID = new ASN1ObjectIdentifier("1.3.101.112");

    private Ed25519PrivateKeyParameters privateKeyParameters;

    /*
     * SEQUENCE (3 elem) INTEGER 0 SEQUENCE (1 elem) OBJECT IDENTIFIER 1.3.101.112
     * curveEd25519 (EdDSA 25519 signature algorithm) OCTET STRING (32 byte)
     * 38AECE974291F14B5FEF97E1B21F684394120B6E7A8AFB04398BBE787E8BC559 OCTET STRING
     * (32 byte) 38AECE974291F14B5FEF97E1B21F684394120B6E7A8AFB04398BBE787E8BC559
     */
    @Override
    public void readPrivateKey(String filename) throws IOException {
        ASN1Primitive key = ASN1Primitive.fromByteArray(PemFileHelper.readPemFile(filename));
        PrivateKeyInfo keyInfo = PrivateKeyInfo.getInstance(key);
        String algoId = keyInfo.getPrivateKeyAlgorithm().getAlgorithm().toString();
        if (algoId.equals(OID.getId())) {
            privateKeyParameters = new Ed25519PrivateKeyParameters(keyInfo.getPrivateKey().getEncoded(), 4);
            setKey(privateKeyParameters.getEncoded());
        }
    }

    @Override
    public void writePrivateKey(String filename) throws IOException {
        DERSequence derPrefix = new DERSequence(OID);
        DEROctetString key = new DEROctetString(new DEROctetString(getKey()));
        ASN1EncodableVector vector = new ASN1EncodableVector();
        vector.add(new ASN1Integer(0));
        vector.add(derPrefix);
        vector.add(key);
        DERSequence derKey = new DERSequence(vector);
        PemFileHelper.writePemFile(filename, derKey.getEncoded(), PRIVATE_KEY_DER_HEADER);
    }

    @Override
    public String sign(String msg) {
        byte[] byteMsg = msg.getBytes();
        Signer signer = new Ed25519Signer();
        signer.init(true, privateKeyParameters);
        signer.update(byteMsg, 0, byteMsg.length);
        byte[] signature;
        try {
            signature = signer.generateSignature();
            return Hex.toHexString(signature);
        } catch (DataLengthException | CryptoException e) {
            // TODO: throw new SomeException();
            return null;
        }
    }

    @Override
    public PublicKey derivePublicKey() {
        return new Ed25519PublicKey(privateKeyParameters.generatePublicKey().getEncoded());
    }
}
