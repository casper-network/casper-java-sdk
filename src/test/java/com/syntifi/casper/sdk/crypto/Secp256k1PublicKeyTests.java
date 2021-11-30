package com.syntifi.casper.sdk.crypto;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Secp256k1PublicKeyTests extends AbstractCryptoTests {
    private static Logger LOGGER = LoggerFactory.getLogger(Secp256k1PublicKeyTests.class);

    @Test
    void readPublicKey_should_load_public_key() throws IOException, URISyntaxException {
        Secp256k1PublicKey pubKey = new Secp256k1PublicKey();
        String filePath = getResourcesKeyPath("crypto/secp256k1/public_key.pem");
        pubKey.readPublicKey(filePath);

        assertNotNull(pubKey.getKey());
    }

    @Test
    void writePublicKey_should_equal_source_file() throws URISyntaxException, IOException {
        Secp256k1PublicKey pubKey = new Secp256k1PublicKey();
        String filePath = getResourcesKeyPath("crypto/secp256k1/public_key.pem");
        pubKey.readPublicKey(filePath);

        DateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss");
        File publicKeyFile = File.createTempFile(df.format(new Date()), "-public-key-test.pem");
        pubKey.writePublicKey(publicKeyFile.getPath());

        assertTrue(compareFiles(Path.of(getResourcesKeyPath("crypto/secp256k1/public_key.pem")).toFile(),
                publicKeyFile));
    }
}