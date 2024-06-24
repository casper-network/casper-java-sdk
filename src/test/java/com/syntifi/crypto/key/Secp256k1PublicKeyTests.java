package com.syntifi.crypto.key;

import com.syntifi.crypto.key.encdec.Hex;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link Secp256k1PublicKey}
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.1.0
 */
public class Secp256k1PublicKeyTests extends AbstractCryptoTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(Secp256k1PublicKeyTests.class);

    @Test
    void readPublicKey_should_load_public_key() throws IOException, URISyntaxException {
        Secp256k1PublicKey pubKey = new Secp256k1PublicKey();
        String filePath = getResourcesKeyPath("secp256k1/public_key.pem");
        pubKey.readPublicKey(filePath);

        assertNotNull(pubKey.getKey());
    }

    @Test
    void writePublicKey_should_equal_source_file() throws URISyntaxException, IOException {
        Secp256k1PublicKey pubKey = new Secp256k1PublicKey();
        String filePath = getResourcesKeyPath("secp256k1/public_key.pem");
        pubKey.readPublicKey(filePath);

        DateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss");
        File publicKeyFile = File.createTempFile(df.format(new Date()), "-public-key-test.pem");
        pubKey.writePublicKey(publicKeyFile.getPath());

        assertTrue(compareTextFiles(new File(getResourcesKeyPath("secp256k1/public_key.pem")),
                publicKeyFile));
    }

    @Test
    void verify_should_be_ok() throws URISyntaxException, IOException, GeneralSecurityException {
        String hexSignature = "ea5b38fd0db5fb3d871c47fde1fa4c4db75d1a9e1c0ac54d826e178ee0e63707176b4e63b4f838bd031f007fffd6a4f71d920a10c48ea53dd1573fa2b58a829e";

        Secp256k1PublicKey pubKey = new Secp256k1PublicKey();
        String filePath = getResourcesKeyPath("secp256k1/public_key.pem");
        pubKey.readPublicKey(filePath);
        LOGGER.info(Hex.encode(pubKey.getKey()));

        assertTrue(pubKey.verify("Test message".getBytes(), Hex.decode(hexSignature)));
    }

    @Test
    void signAndRecoverPublicKey_1() throws URISyntaxException, IOException {

        //Get the private key
        Secp256k1PrivateKey privKey = new Secp256k1PrivateKey();
        String filePath = getResourcesKeyPath("secp256k1/secret_key.pem");
        privKey.readPrivateKey(filePath);

        //Derive the public key
        Secp256k1PublicKey publicKey = (Secp256k1PublicKey) privKey.derivePublicKey();

        String message = "bc81ca4de9b3a991a6514eddf0e994e0035c7ba58f333c4d7ba5dd18b4c9c547";

        //Generate the signature
        byte[] signature = privKey.sign(message.getBytes());

        //Test
        assert publicKey.verify(message.getBytes(), signature);

    }

    @Test
    void signAndRecoverPublicKey_2() throws URISyntaxException, IOException {

        //Get the private key
        Secp256k1PrivateKey privKey = new Secp256k1PrivateKey();

        String filePath = getResourcesKeyPath("secp256k1/secret_key.pem");
        privKey.readPrivateKey(filePath);

        //Derive the public key
        Secp256k1PublicKey publicKey = (Secp256k1PublicKey) privKey.derivePublicKey();

        String message = "1df13c9aaa8217657b7e5ec2442594735eeb4ca7e764877b3d2b593c3909d15f";

        //Generate the signature
        byte[] signature = privKey.sign(message.getBytes());

        //Test
        assertTrue(publicKey.verify(message.getBytes(), signature));

    }

    @Test
    void signAndRecoverPublicKey_3() throws URISyntaxException, IOException {

        //Get the private key
        Secp256k1PrivateKey privKey = new Secp256k1PrivateKey();
        String filePath = getResourcesKeyPath("secp256k1/secret_key.pem");
        privKey.readPrivateKey(filePath);

        //Derive the public key
        Secp256k1PublicKey publicKey = (Secp256k1PublicKey) privKey.derivePublicKey();

        String message = "Test message";

        //Generate the signature
        byte[] signature = privKey.sign(message.getBytes());

        //Test
        assertTrue(publicKey.verify(message.getBytes(), signature));

    }

    @Test
    void signAndRecoverPublicKey_4() throws URISyntaxException, IOException {

        //Get the private key
        Secp256k1PrivateKey privKey = new Secp256k1PrivateKey();
        String filePath = getResourcesKeyPath("secp256k1/secret_key.pem");
        privKey.readPrivateKey(filePath);

        //Derive the public key
        Secp256k1PublicKey publicKey = (Secp256k1PublicKey) privKey.derivePublicKey();

        String message = "Test message";

        //Generate the signature
        byte[] signature = privKey.sign(message.getBytes());

        //Test
        assertTrue(publicKey.verify(message.getBytes(), signature));
        assertFalse(publicKey.verify("Not test message".getBytes(), signature));
    }
}
