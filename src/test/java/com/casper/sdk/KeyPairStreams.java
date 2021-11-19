package com.casper.sdk;

import java.io.InputStream;

public class KeyPairStreams {

    private final InputStream publicKeyIn;
    private final InputStream privateKeyIn;

   public KeyPairStreams(InputStream publicKeyIn, InputStream privateKeyIn) {
        this.publicKeyIn = publicKeyIn;
        this.privateKeyIn = privateKeyIn;
    }

    public InputStream getPublicKeyIn() {
        return publicKeyIn;
    }

    public InputStream getPrivateKeyIn() {
        return privateKeyIn;
    }
}
