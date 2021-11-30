package com.syntifi.casper.sdk.crypto;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SignatureException;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.abi.datatypes.Bool;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
import org.web3j.crypto.Sign.SignatureData;

public class Secp256k1PrivateKey extends PrivateKey {

    private ECKeyPair keyPair;

    @Override
    public void readPrivateKey(String filename) throws IOException {
        ASN1Sequence key = (ASN1Sequence) ASN1Primitive.fromByteArray(PemFileHelper.readPemFile(filename));
        String algoId = key.getObjectAt(2).toString();
        if (algoId.equals("[0]" + ASN1Identifiers.Secp256k1OIDCurve) && key.getObjectAt(0).toString().equals("1")) {
            DEROctetString pk = (DEROctetString) key.getObjectAt(1);
            Credentials cs = Credentials.create(Hex.toHexString(pk.getEncoded()));
            keyPair = cs.getEcKeyPair();
            this.setKey(keyPair.getPrivateKey().toByteArray());
        }
    }

    @Override
    public void writePrivateKey(String filename) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            DERTaggedObject derPrefix = new DERTaggedObject(0, ASN1Identifiers.Secp256k1OIDCurve);
            DEROctetString key = new DEROctetString(keyPair.getPrivateKey().toByteArray());
            ASN1EncodableVector vector = new ASN1EncodableVector();
            vector.add(new ASN1Integer(1));
            vector.add(key);
            vector.add(derPrefix);
            DERSequence derKey = new DERSequence(vector);
            PemFileHelper.writePemFile(fileWriter, derKey.getEncoded(), ASN1Identifiers.EC_PRIVATE_KEY_DER_HEADER);
        }
    }

    @Override
    public String sign(String msg) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * public SignatureData sign(String msg) {
     * SignatureData signature = Sign.signMessage(Hash.sha256(msg.getBytes()),
     * keyPair, false);
     * return signature;
     * }
     */

    public Bool verify(String msg, SignatureData signature) throws SignatureException {
        BigInteger publicKey = Sign.signedMessageToKey(Hash.sha256(msg.getBytes()), signature);
        return new Bool(publicKey.equals(keyPair.getPublicKey()));
    }

    @Override
    public Boolean verify(String msg, String hex) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public PublicKey derivePublicKey() {
        return new Secp256k1PublicKey(keyPair.getPublicKey().toByteArray());
    }

}
