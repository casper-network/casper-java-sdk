package com.syntifi.casper.sdk.crypto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

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
        Path hexKeyFilePath = Path.of(getResourcesKeyPath("crypto/Ed25519/public_key_hex").substring(1));
        String hexKey = Files.readString(hexKeyFilePath);
        LOGGER.debug("Hex Key from {}: {}", hexKeyFilePath, Hex.toHexString(pubKey.getKey()));
        assertEquals(hexKey.substring(2), Hex.toHexString(pubKey.getKey()));
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
