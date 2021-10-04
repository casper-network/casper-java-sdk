package com.casper.sdk;

import com.casper.sdk.types.SignatureAlgorithm;
import com.casper.sdk.types.CLPublicKey;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;

public class IntegrationTestUtils {

    static class KeyPairStreams {

        private final InputStream publicKeyIn;
        private final InputStream privateKeyIn;

        KeyPairStreams(InputStream publicKeyIn, InputStream privateKeyIn) {
            this.publicKeyIn = publicKeyIn;
            this.privateKeyIn = privateKeyIn;
        }

        public InputStream getPublicKeyIn() {
            return publicKeyIn;
        }

        public InputStream getPrivateKeyIn() {
            return privateKeyIn;
        }
    }

    private static final String NCTL_HOME = "~/Documents/casper/casper-node/utils/nctl";

    static String getPublicKeyAccountHex(final KeyPair keyPair) {

        final CLPublicKey publicKey = new CLPublicKey(keyPair.getPublic().getEncoded(), SignatureAlgorithm.ED25519);
        return publicKey.toAccountHex();
    }


    static KeyPairStreams geUserKeyPairStreams(int userNumber) throws IOException {

        final String userNPath = String.format("%s/assets/net-1/users/user-%d", getNctlHome(), userNumber);
        final FileInputStream publicKeyIn = new FileInputStream(new File(userNPath, "public_key.pem"));
        final FileInputStream privateKeyIn = new FileInputStream(new File(userNPath, "secret_key.pem"));
        return new KeyPairStreams(publicKeyIn, privateKeyIn);
    }


    static KeyPairStreams getNodeKeyPairSteams(final int nodeNumber) throws IOException {

        final String userNPath = String.format("%s/assets/net-1/nodes/node-%d/keys", getNctlHome(), nodeNumber);
        final FileInputStream publicKeyIn = new FileInputStream(new File(userNPath, "public_key.pem"));
        final FileInputStream privateKeyIn = new FileInputStream(new File(userNPath, "secret_key.pem"));
        return new KeyPairStreams(publicKeyIn, privateKeyIn);
    }

    static String getNctlHome() {
        String nctlHome = System.getProperty("nctl.home");
        if (nctlHome == null) {
            nctlHome = NCTL_HOME;
        }

        // Replace user home '~' tilda with full path to user home directory
        nctlHome = nctlHome.replaceFirst("^~", System.getProperty("user.home"));

        return nctlHome;
    }


}
