package com.syntifi.casper.sdk.crypto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
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
        Path hexKeyFilePath = Paths.get(getResourcesKeyPath("crypto/Ed25519/public_key_hex"));
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

        assertTrue(compareTextFiles(new File(getResourcesKeyPath("crypto/Ed25519/public_key.pem")),
                publicKeyFile));
    }

    @Test
    void verify_should_be_ok() throws URISyntaxException, IOException, GeneralSecurityException {
        String message = "Test message";
        String hexSignature = "4555103678684364a98478112ce0c298ed841d806d2b67b09e8f0215cc738f3c5a1fca5beaf0474ff636613821bcb97e88b3b4d700e65c6cf7574489e09f170c";

        Ed25519PublicKey pubKey = loadPublicKey("crypto/Ed25519/public_key.pem");

        String hexKey = Hex.toHexString(pubKey.getKey());

        LOGGER.debug("Verifying Ed25519 signature of {} with key {}", message, hexKey);

        Boolean verified = pubKey.verify(message, hexSignature);

        LOGGER.debug("Signature verified: {}", verified);

        assertTrue(verified);
    }

    @Test
    void verify_unicode_string() throws GeneralSecurityException {
        String message = "أبو يوسف يعقوب بن إسحاق الصبّاح الكندي‎";
        String hexKey = "cd62f1c5cca51fa3c25f4c76a46dd5f6b0988c95da6ea835ec4441d68dcea393";
        String hexSignature = "c507aa390d6779d949d38258d6e1c8509664c16e645bdda9e70365ae06b23944b041aee323ea3783c923d90c73efa9c59d7b1ed87a5a1332a245874bd944fb07";

        LOGGER.debug("Verifying Ed25519 signature of {} with key {}", message, hexKey);

        Ed25519PublicKey pubKey = new Ed25519PublicKey(Hex.decode(hexKey));

        Boolean verified = pubKey.verify(message, hexSignature);

        LOGGER.debug("Signature verified: {}", verified);

        assertTrue(verified);
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
