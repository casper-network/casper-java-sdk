package com.casper.sdk.service.signing;


import java.security.KeyPair;

public interface Signer {

    KeyPair generateKeyPair();

    SignatureAlgorithm getAlgorithm();
}
