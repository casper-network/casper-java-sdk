package com.syntifi.casper.sdk.crypto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Secp256k1PrivateKeyTests extends AbstractCryptoTests {

    private static Logger LOGGER = LoggerFactory.getLogger(Secp256k1PublicKeyTests.class);

    @Test
    void readPrivateKey_should_load_private_key() throws URISyntaxException, IOException {
        Secp256k1PrivateKey privKey = new Secp256k1PrivateKey();
        String filePath = getResourcesKeyPath("crypto/secp256k1/secret_key.pem");
        privKey.readPrivateKey(filePath);

        assertNotNull(privKey.getKey());
    }

    @Test
    void writePrivateKey_should_equal_source_file() throws URISyntaxException, IOException {
        Secp256k1PrivateKey privKey = new Secp256k1PrivateKey();
        String filePath = getResourcesKeyPath("crypto/secp256k1/secret_key.pem");
        privKey.readPrivateKey(filePath);

        DateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss");
        File privateKeyFile = File.createTempFile(df.format(new Date()), "-secret-key-test.pem");
        privKey.writePrivateKey(privateKeyFile.getPath());

        assertTrue(compareTextFiles(new File(getResourcesKeyPath("crypto/secp256k1/secret_key.pem")),
                privateKeyFile));
    }

    @Test
    void readPrivateKey_derived_public_key_should_equal_generated() throws URISyntaxException, IOException {
        Secp256k1PrivateKey privKey = new Secp256k1PrivateKey();
        String filePath = getResourcesKeyPath("crypto/secp256k1/secret_key.pem");
        privKey.readPrivateKey(filePath);
        Secp256k1PublicKey pubKey = (Secp256k1PublicKey) privKey.derivePublicKey();

        DateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss");
        File derivedPublicKeyFile = File.createTempFile(df.format(new Date()), "-derived-public-key-test.pem");
        pubKey.writePublicKey(derivedPublicKeyFile.getPath());
        LOGGER.info(privKey.getKeyPair().getPrivateKey().toString(16));
        LOGGER.info(privKey.getKeyPair().getPublicKey().toString(16));
        LOGGER.info(Hex.toHexString(pubKey.getKey()));
        assertTrue(compareTextFiles(new File(getResourcesKeyPath("crypto/secp256k1/public_key.pem")),
                derivedPublicKeyFile));
    }

    @Test
    void sign_should_sign_message() throws URISyntaxException, IOException {
        Secp256k1PrivateKey privKey = new Secp256k1PrivateKey();
        String filePath = getResourcesKeyPath("crypto/secp256k1/secret_key.pem");
        privKey.readPrivateKey(filePath);
        LOGGER.info(privKey.getKeyPair().getPublicKey().toString(16));
        LOGGER.info(Hex.toHexString(privKey.getKey()));

        String signature = privKey.sign("Test message");

        LOGGER.info(signature);
        assertEquals(
            "ea5b38fd0db5fb3d871c47fde1fa4c4db75d1a9e1c0ac54d826e178ee0e63707176b4e63b4f838bd031f007fffd6a4f71d920a10c48ea53dd1573fa2b58a829e",
                signature);
    }
}
