package com.syntifi.casper.sdk.crypto;

import java.io.IOException;

public class Secp256k1PublicKey extends PublicKey {

    public Secp256k1PublicKey(byte[] bytes) {
        super(bytes);
    }

    @Override
    public void readPublicKey(String filename) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String sign(String msg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void writePublicKey(String filename) throws IOException {
        // TODO Auto-generated method stub
        
    }
    
}
