package com.casper.sdk.service.signing;


import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

import java.io.InputStream;

public interface Signer {

    AsymmetricCipherKeyPair generateKeyPair();

    AsymmetricCipherKeyPair loadKeyPair(final InputStream publicKeyIn, final InputStream privateKeyIn);

    byte[] signWithPrivateKey(final AsymmetricKeyParameter privateKey, final byte[] toSign);

    boolean verifySignature(final AsymmetricKeyParameter publicKey, final byte[] toSign, final byte[] signature);

    SignatureAlgorithm getAlgorithm();
}
