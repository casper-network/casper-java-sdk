package com.syntifi.crypto.key;

import com.syntifi.crypto.key.encdec.Hex;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Ed25519PublicKey}
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.1.0
 */
public class Ed25519PublicKeyTests extends AbstractCryptoTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(Ed25519PublicKeyTests.class);

    @Test
    void readPublicKey_should_load_and_be_equal_to_generated_public_key() throws IOException, URISyntaxException {
        Ed25519PublicKey publicKey = loadPublicKey("ed25519/public_key.pem");
        assertNotNull(publicKey.getKey());

        // Compare to generated hex without leading id byte
        Path hexKeyFilePath = Paths.get(getResourcesKeyPath("ed25519/public_key_hex"));
        String hexKey = new String(Files.readAllBytes(hexKeyFilePath));
        LOGGER.debug("Hex Key from {}: {}", hexKeyFilePath, Hex.encode(publicKey.getKey()));
        assertEquals(hexKey.substring(2), Hex.encode(publicKey.getKey()));
    }

    @Test
    void writePublicKey_should_equal_source_file() throws URISyntaxException, IOException {
        Ed25519PublicKey publicKey = loadPublicKey("ed25519/public_key.pem");

        DateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss");
        File publicKeyFile = File.createTempFile(df.format(new Date()), "-public-key-test.pem");

        LOGGER.debug("Writing public key to {}", publicKeyFile.getPath());
        publicKey.writePublicKey(publicKeyFile.getPath());

        assertTrue(compareTextFiles(new File(getResourcesKeyPath("ed25519/public_key.pem")),
                publicKeyFile));
    }

    @Test
    void verify_should_work_with_previously_signed_message() throws URISyntaxException, IOException {
        String message = "Test message";
        String hexSignature = "4555103678684364a98478112ce0c298ed841d806d2b67b09e8f0215cc738f3c5a1fca5beaf0474ff636613821bcb97e88b3b4d700e65c6cf7574489e09f170c";

        Ed25519PublicKey publicKey = loadPublicKey("ed25519/public_key.pem");

        String hexKey = Hex.encode(publicKey.getKey());

        LOGGER.debug("Verifying Ed25519 signature of {} with key {}", message, hexKey);

        Boolean verified = publicKey.verify(message.getBytes(), Hex.decode(hexSignature));

        LOGGER.debug("Signature verified: {}", verified);

        assertTrue(verified);
    }

    private Ed25519PublicKey loadPublicKey(String publicKeyPath) throws URISyntaxException, IOException {
        Ed25519PublicKey publicKey = new Ed25519PublicKey();
        String keyFilePath = getResourcesKeyPath(publicKeyPath);
        LOGGER.debug("Reading key from {}", keyFilePath);
        publicKey.readPublicKey(keyFilePath);
        LOGGER.debug("Key: {}", Hex.encode(publicKey.getKey()));
        return publicKey;
    }
}
