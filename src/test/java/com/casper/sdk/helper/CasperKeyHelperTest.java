package com.casper.sdk.helper;

import com.casper.sdk.model.key.AlgorithmTag;
import com.syntifi.crypto.key.Ed25519PrivateKey;
import com.syntifi.crypto.key.Ed25519PublicKey;
import com.syntifi.crypto.key.Secp256k1PrivateKey;
import com.syntifi.crypto.key.Secp256k1PublicKey;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.junit.jupiter.api.Assertions.*;

public class CasperKeyHelperTest {

    @Test
    void createRandomKeysEd25519() {
        Ed25519PrivateKey sk = CasperKeyHelper.createRandomEd25519Key();
        Ed25519PublicKey pk = CasperKeyHelper.derivePublicKey(sk);
        byte[] msg = "this is a test".getBytes();
        byte[] signature = sk.sign(msg);
        assertTrue(pk.verify(msg, signature));
    }

    @Test
    void createRandomKeysSecp25516() throws GeneralSecurityException, IOException {
        Secp256k1PrivateKey sk = CasperKeyHelper.createRandomSecp256k1Key();
        Secp256k1PublicKey pk = CasperKeyHelper.derivePublicKey(sk);
        byte[] msg = "this is a test".getBytes();
        byte[] signature = sk.sign(msg);
        assertTrue(pk.verify(msg, signature));
    }
}
