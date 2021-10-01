package com.casper.sdk.service.signing;


import com.casper.sdk.types.SignatureAlgorithm;

import java.security.KeyPair;
import java.security.PublicKey;

public interface KeyPairBuilder {

    KeyPair generateKeyPair();

    SignatureAlgorithm getAlgorithm();

    boolean isSupportedPublicKey(final PublicKey publicKey);

    byte [] getPublicKeyRawBytes(final PublicKey publicKey);
}
