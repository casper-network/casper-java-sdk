package com.syntifi.casper.sdk.crypto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ed25519PrivateKeyTests extends AbstractCryptoTests {
    private static Logger LOGGER = LoggerFactory.getLogger(Ed25519PrivateKeyTests.class);

    @Test
    void readPrivateKey_should_load_private_key() throws IOException, URISyntaxException {
        Ed25519PrivateKey privKey = loadPrivateKey("crypto/Ed25519/secret_key.pem");
        assertNotNull(privKey.getKey());
    }

    @Test
    void readPrivateKey_derived_public_key_should_equal_generated() throws IOException, URISyntaxException {
        Ed25519PrivateKey privKey = loadPrivateKey("crypto/Ed25519/secret_key.pem");

        // Compare derived public key to generated hex without leading id byte
        Path hexKeyFilePath = Path.of(getResourcesKeyPath("crypto/Ed25519/public_key_hex").substring(1));
        String hexKey = Files.readString(hexKeyFilePath);
        LOGGER.debug("Derived public hex Key from {}: {}", hexKeyFilePath,
                Hex.toHexString(privKey.derivePublicKey().getKey()));

        assertEquals(hexKey.substring(2), Hex.toHexString(privKey.derivePublicKey().getKey()));
    }

    @Test
    void writePrivateKey_should_equal_source_file() throws URISyntaxException, IOException {
        Ed25519PrivateKey privKey = loadPrivateKey("crypto/Ed25519/secret_key.pem");

        DateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss");
        File privateKeyFile = File.createTempFile(df.format(new Date()), "-private-key-test.pem");

        LOGGER.debug("Writing private key to {}", privateKeyFile.getPath());
        privKey.writePrivateKey(privateKeyFile.getPath());

        assertTrue(compareFiles(Path.of(getResourcesKeyPath("crypto/Ed25519/secret_key.pem").substring(1)).toFile(),
                privateKeyFile));
    }

    private Ed25519PrivateKey loadPrivateKey(String privateKeyPath) throws URISyntaxException, IOException {
        Ed25519PrivateKey privKey = new Ed25519PrivateKey();
        String keyFilePath = getResourcesKeyPath(privateKeyPath);
        LOGGER.debug("Reading key from {}", keyFilePath);
        privKey.readPrivateKey(keyFilePath);
        LOGGER.debug("Key: {}", Hex.toHexString(privKey.getKey()));
        return privKey;
    }
}
