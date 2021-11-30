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
        Secp256k1PrivateKey pvtKey = new Secp256k1PrivateKey();
        String filePath = getResourcesKeyPath("crypto/secp256k1/secret_key.pem");
        pvtKey.readPrivateKey(filePath);

        assertNotNull(pvtKey.getKey());
    }

    @Test
    void writePublicKey_should_equal_source_file() throws URISyntaxException, IOException {
        Secp256k1PrivateKey pubKey = new Secp256k1PrivateKey();
        String filePath = getResourcesKeyPath("crypto/secp256k1/secret_key.pem");
        pubKey.readPrivateKey(filePath);

        DateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss");
        File publicKeyFile = File.createTempFile(df.format(new Date()), "-secret-key-test.pem");
        pubKey.writePrivateKey(publicKeyFile.getPath());

        assertTrue(compareFiles(Path.of(getResourcesKeyPath("crypto/secp256k1/secret_key.pem")).toFile(),
                publicKeyFile));
    }
}
