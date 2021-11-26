package com.syntifi.casper.sdk.crypto;

import java.io.IOException;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.web3j.abi.datatypes.Bool;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Ed25519PublicKey extends PublicKey {
    public static final String PUBLIC_KEY_DER_HEADER = "PUBLIC KEY";

    // 1.3.101.112 is the OID identifier for the Ed25519 schema
    private static final ASN1ObjectIdentifier OID = new ASN1ObjectIdentifier("1.3.101.112");

    private Ed25519PublicKeyParameters publicKeyParameters;

    public Ed25519PublicKey(byte[] bytes) {
        super(bytes);
    }

    /*
     * SEQUENCE (2 elem) SEQUENCE (1 elem) OBJECT IDENTIFIER 1.3.101.112
     * curveEd25519 (EdDSA 25519 signature algorithm) BIT STRING (256 bit) <KEY
     * HERE>
     */
    @Override
    public void readPublicKey(String filename) throws IOException {
        ASN1Primitive derKey = ASN1Primitive.fromByteArray(PemFileHelper.readPemFile(filename));
        ASN1Sequence objBaseSeq = ASN1Sequence.getInstance(derKey);
        String objId = ASN1ObjectIdentifier
                .getInstance(ASN1Sequence.getInstance(objBaseSeq.getObjectAt(0)).getObjectAt(0)).getId();
        if (objId.equals(OID.getId())) {
            DERBitString key = DERBitString.getInstance(objBaseSeq.getObjectAt(1));
            publicKeyParameters = new Ed25519PublicKeyParameters(key.getBytes(), 0);
            setKey(publicKeyParameters.getEncoded());
        }
    }

    @Override
    public void writePublicKey(String filename) throws IOException {
        DERSequence derPrefix = new DERSequence(OID);
        DERBitString key = new DERBitString(getKey());
        ASN1EncodableVector vector = new ASN1EncodableVector();
        vector.add(derPrefix);
        vector.add(key);
        DERSequence derKey = new DERSequence(vector);
        PemFileHelper.writePemFile(filename, derKey.getEncoded(), PUBLIC_KEY_DER_HEADER);
    }

    @Override
    public String sign(String msg) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public Bool verify(String msg) {
        // TODO Auto-generated method stub
        return null;
    }
}
