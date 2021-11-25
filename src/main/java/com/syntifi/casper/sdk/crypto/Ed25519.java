package com.syntifi.casper.sdk.crypto;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;

import lombok.Data;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@Data
public class Ed25519 {

    private Ed25519PrivateKeyParameters privateKey; 
    private Ed25519PublicKeyParameters publicKey; 
    //1.3.101.112 is the OID identifier for the Ed25519 schema 
    private static final ASN1ObjectIdentifier OID = new ASN1ObjectIdentifier("1.3.101.112");

    private byte[] readPemFile(String filename) throws IOException {
        try (FileReader keyReader = new FileReader(filename);
            PemReader pemReader = new PemReader(keyReader)) {
                var pemObject = pemReader.readPemObject();
                return pemObject.getContent();
            }
    }

    private void writePemFile(String filename, byte[] encodedKey, String keyType) throws IOException {
        try (PemWriter pemWriter = new PemWriter(new FileWriter(filename))) {
            pemWriter.writeObject(new PemObject(keyType, encodedKey) );
        }
    }

    /*
    SEQUENCE (3 elem)
      INTEGER 0
      SEQUENCE (1 elem)
        OBJECT IDENTIFIER 1.3.101.112 curveEd25519 (EdDSA 25519 signature algorithm)
      OCTET STRING (32 byte) 38AECE974291F14B5FEF97E1B21F684394120B6E7A8AFB04398BBE787E8BC559
        OCTET STRING (32 byte) 38AECE974291F14B5FEF97E1B21F684394120B6E7A8AFB04398BBE787E8BC559
    */
    public void readPrivateKey(String filename) throws IOException {
        ASN1Primitive key = ASN1Primitive.fromByteArray(readPemFile(filename));
        var keyInfo = PrivateKeyInfo.getInstance(key);
        String algoId = keyInfo.getPrivateKeyAlgorithm().getAlgorithm().toString();
        if (algoId.equals(OID.getId())) {
            privateKey = new Ed25519PrivateKeyParameters(keyInfo.getPrivateKey().getEncoded(), 4);
        }
    }

    public void writePrivateKey(String filename) throws IOException {
        var derPrefix = new DERSequence(OID);
        var key = new DEROctetString(new DEROctetString(privateKey.getEncoded()));
        var vector = new ASN1EncodableVector();
        vector.add(new ASN1Integer(0));
        vector.add(derPrefix);
        vector.add(key);
        var derKey = new DERSequence(vector);
        writePemFile(filename, derKey.getEncoded(), "PRIVATE KEY");
    }

    /*
    SEQUENCE (2 elem)
      SEQUENCE (1 elem)
        OBJECT IDENTIFIER 1.3.101.112 curveEd25519 (EdDSA 25519 signature algorithm)
      BIT STRING (256 bit) <KEY HERE> 
    */
    public void readPublicKey(String filename) throws IOException {
        ASN1Primitive derKey = ASN1Primitive.fromByteArray(readPemFile(filename));
        var objBaseSeq = ASN1Sequence.getInstance(derKey);
        String objId = ASN1ObjectIdentifier.getInstance(ASN1Sequence.getInstance(objBaseSeq.getObjectAt(0)).getObjectAt(0)).getId();
        if (objId.equals(OID.getId())) {
            var key = DERBitString.getInstance(objBaseSeq.getObjectAt(1));
            publicKey = new Ed25519PublicKeyParameters(key.getBytes(), 0);
        }
    }

    public void writePublicKey(String filename) throws IOException {
        var derPrefix = new DERSequence(OID);
        var key = new DERBitString(publicKey.getEncoded());
        var vector = new ASN1EncodableVector();
        vector.add(derPrefix);
        vector.add(key);
        var derKey = new DERSequence(vector);
        writePemFile(filename, derKey.getEncoded() , "PUBLIC KEY");
    }

    public Ed25519PublicKeyParameters derivePublicKey() {
        return privateKey.generatePublicKey();
    }

    public String getHexPublicKey() {
        String hexPublicKey = Hex.toHexString(publicKey.getEncoded());
        return "01" + hexPublicKey;
    }

}
