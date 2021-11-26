package com.syntifi.casper.sdk.crypto;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ed25519PublicKeyTests extends AbstractCryptoTests {
    private static Logger LOGGER = LoggerFactory.getLogger(Ed25519PublicKeyTests.class);

    @Test
    void readPublicKey_should_load_public_key() throws IOException, URISyntaxException {
        Ed25519PublicKey pubKey = new Ed25519PublicKey();
        String filePath = getResourcesKeyPath("crypto/Ed25519/public_key.pem");
        pubKey.readPublicKey(filePath);

        assertNotNull(pubKey.key);
    }
}
