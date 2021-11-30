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

public class Ed25519PublicKeyTests extends AbstractCryptoTests {
    private static Logger LOGGER = LoggerFactory.getLogger(Ed25519PublicKeyTests.class);

    @Test
    void readPublicKey_should_load_and_be_equal_to_generated_public_key() throws IOException, URISyntaxException {
        Ed25519PublicKey pubKey = loadPublicKey("crypto/Ed25519/public_key.pem");                
        assertNotNull(pubKey.getKey());

        // Compare to generated hex without leading id byte
        Path hexKeyFilePath = Path.of(getResourcesKeyPath("crypto/Ed25519/public_key_hex"));
        String hexKey = Files.readString(hexKeyFilePath);
        LOGGER.debug("Hex Key from {}: {}", hexKeyFilePath, Hex.toHexString(pubKey.getKey()));
        assertEquals(hexKey.substring(2), Hex.toHexString(pubKey.getKey()));
    }

    @Test
    void writePublicKey_should_equal_source_file() throws URISyntaxException, IOException {
        Ed25519PublicKey pubKey = loadPublicKey("crypto/Ed25519/public_key.pem");

        DateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss");
        File publicKeyFile = File.createTempFile(df.format(new Date()), "-public-key-test.pem");

        LOGGER.debug("Writing public key to {}", publicKeyFile.getPath());
        pubKey.writePublicKey(publicKeyFile.getPath());

        assertTrue(compareTextFiles(Path.of(getResourcesKeyPath("crypto/Ed25519/public_key.pem")).toFile(),
                publicKeyFile));
    }

    @Test
    void verify_should_be_ok() throws URISyntaxException, IOException {
        String hexSignature = "4555103678684364a98478112ce0c298ed841d806d2b67b09e8f0215cc738f3c5a1fca5beaf0474ff636613821bcb97e88b3b4d700e65c6cf7574489e09f170c";

        Ed25519PublicKey pubKey = loadPublicKey("crypto/Ed25519/public_key.pem");

        assertTrue(pubKey.verify("Test message", hexSignature));
    }

    private Ed25519PublicKey loadPublicKey(String publicKeyPath) throws URISyntaxException, IOException {
        Ed25519PublicKey pubKey = new Ed25519PublicKey();        
        String keyFilePath = getResourcesKeyPath(publicKeyPath);
        LOGGER.debug("Reading key from {}", keyFilePath);
        pubKey.readPublicKey(keyFilePath);
        LOGGER.debug("Key: {}", Hex.toHexString(pubKey.getKey()));
        return pubKey;
    }
}
