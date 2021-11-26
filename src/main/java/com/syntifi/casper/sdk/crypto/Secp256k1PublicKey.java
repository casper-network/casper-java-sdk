package com.syntifi.casper.sdk.crypto;

import java.io.IOException;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.web3j.abi.datatypes.Bool;

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
        // TODO Auto-generated method stub
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
