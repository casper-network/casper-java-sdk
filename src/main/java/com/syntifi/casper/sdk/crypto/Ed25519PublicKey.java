package com.syntifi.casper.sdk.crypto;

import java.io.IOException;

public class Ed25519PublicKey extends PublicKey {

    public Ed25519PublicKey(byte[] bytes) {
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

}
