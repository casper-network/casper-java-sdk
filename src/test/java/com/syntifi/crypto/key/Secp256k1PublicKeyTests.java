package com.syntifi.crypto.key;

import com.casper.sdk.model.key.PublicKey;
import com.syntifi.crypto.key.encdec.Hex;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
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
    void verify_should_be_ok() throws URISyntaxException, IOException {
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
        assert publicKey.verify(message.getBytes(), signature);

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
        assert publicKey.verify(message.getBytes(), signature);

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
        assert publicKey.verify(message.getBytes(), signature);
        assert !publicKey.verify("Not test message".getBytes(), signature);

    }

    @Test
    void signAndRecoverPublicKey_5() throws GeneralSecurityException {

        String publicKey = "02027fec2d969dd0779358c40abe2a772f309408348c2a1f413fddfe684a4287ba1a";
        String signature = "2f26d3a896e5ad74bafc381455fc9bec808cea64b083b4a05e3dae43d80478820f59128c97bb50b52d5eba90eb29ec8dbd1b39fe6488bbc8c44a799e9a631878";
        String hash = "92c983f3bb205b4f642f532dff837ebed56b19f11351da9181f6512e75a54a9b";

        PublicKey key = new PublicKey();
        key.createPublicKey(publicKey);

        assert key.getPubKey().verify(Hex.decode(hash), Hex.decode(signature));

    }

    @Test
    void signAndRecoverPublicKey_6() throws GeneralSecurityException {

        String publicKey = "02024c5e3ba7b1da49cda950319aec914cd3c720fbec3dcf25aa4add631e28f70aa9";
        String signature = "ade1d9902ec5520e0f52052b329cc62620ac5e84294729c559ce979fe0cf22aa29a8cef7a9d985851da01f68b4049ad9db3d310440cafcb3b5fe40ed6040aa1e";
        String hash = "57a2fc930ab83f6bf5b3f1d7d5d1bd05ccd20dcbe3135f7c21a0553650d220e6";

        PublicKey key = new PublicKey();
        key.createPublicKey(publicKey);

        assert key.getPubKey().verify(Hex.decode(hash), Hex.decode(signature));

    }

    @Test
    void signAndRecoverPublicKey_7() throws GeneralSecurityException {

        String publicKey = "0203b34ffdb855f8654f7f3805257844fe08d4055bcf75bbbb19b78463e34ba8b932";
        String signature = "29d7ea92f1193c13c7ab5d90dfdfab75f52fd49047b9bb4fafc9962a7a1e0d2a36828d93d0d95080f53c625a2f2111dd86c37391f9a05a7ea5a1f2abba579476";
        String hash = "3fa94f85c55a44a8afc2944914eb38720cc85d4b130809f8409a8c7e3e677298";

        PublicKey key = new PublicKey();
        key.createPublicKey(publicKey);

        assert key.getPubKey().verify(Hex.decode(hash), Hex.decode(signature));

    }

    @Test
    void signAndRecoverPublicKey_8() throws GeneralSecurityException {

        String publicKey = "020287a38f11ac7aab689205912b981c21b80cf1efdd7541bd09cbe1e6ad6e8f188c";
        String signature = "d1a796f1221e2256dbe85904e2c3d07be52c1482f8b3cfdce0bef0b6d4c5fc5537e3d8720054888eba9ddcb4c6ff388fabb5c09143000980e6af9096ee163524";
        String hash = "1df13c9aaa8217657b7e5ec2442594735eeb4ca7e764877b3d2b593c3909d15f";

        PublicKey key = new PublicKey();
        key.createPublicKey(publicKey);

        assert key.getPubKey().verify(Hex.decode(hash), Hex.decode(signature));

    }

    @Test
    void signAndRecoverPublicKey_9() throws GeneralSecurityException {
        String publicKey = "0202ea6c7f7f281078af87e79a3856d500da39d35743d8253c9bc1fce7b1da0a3536";
        String signature = "c808c81f8db9a3a7364b0d884968c74427ace3fbd66cef9a8f0235e570b2774407071b63094446992e37c8f0e7d154bacd882d583fa2bfb043c884e445da56aa";
        String hash = "e96cb427900710e89e39f3a69b8f576c8b5cb3085702f3c33c46fcbb115f1775";

        PublicKey key = new PublicKey();
        key.createPublicKey(publicKey);

        assert key.getPubKey().verify(Hex.decode(hash), Hex.decode(signature));

    }


}
