package com.syntifi.casper.sdk.crypto;

import java.io.IOException;

import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.ECKeyPair;

public class Secp256k1PrivateKey extends PrivateKey {

    private ECKeyPair keyPair;

    @Override
    public void readPrivateKey(String filename) throws IOException {
        ASN1Sequence key = ASN1Sequence.getInstance(PemFileHelper.readPemFile(filename));
        PrivateKeyInfo keyInfo = PrivateKeyInfo.getInstance(key.getObjectAt(1));
        String algoId = keyInfo.getPrivateKeyAlgorithm().getAlgorithm().toString();
        if (algoId.equals(ASN1Identifiers.Secp256k1OIDkey.getId())) {
            Credentials cs = Credentials.create(Hex.toHexString(keyInfo.getEncoded()));
            keyPair = cs.getEcKeyPair();
        }
    }

    public void writePrivateKey(String filename) throws IOException {
        // TODO Auto-generated method stub
    }

    @Override
    public String sign(String msg) {
        ECDSASignature signature = keyPair.sign(msg.getBytes());
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PublicKey derivePublicKey() {
        return new Secp256k1PublicKey(keyPair.getPublicKey().toByteArray());
    }

}
