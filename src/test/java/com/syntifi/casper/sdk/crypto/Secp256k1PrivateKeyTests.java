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
}
