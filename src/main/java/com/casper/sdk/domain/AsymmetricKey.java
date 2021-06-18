package com.casper.sdk.domain;

public class AsymmetricKey {

    private final PublicKey publicKey;
    private final byte[] privateKey;
    private final KeyAlgorithm keyAlgorithm;

    public AsymmetricKey(final PublicKey publicKey,
                         final byte[] privateKey,
                         final KeyAlgorithm keyAlgorithm) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.keyAlgorithm = keyAlgorithm;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public byte[] getPrivateKey() {
        return privateKey;
    }

    public KeyAlgorithm getSignatureAlgorithm() {
        return keyAlgorithm;
    }

    public byte [] sign(Digest hash) {
        return null;
    }
}
