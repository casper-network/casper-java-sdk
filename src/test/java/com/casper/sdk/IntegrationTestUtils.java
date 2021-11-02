package com.casper.sdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class IntegrationTestUtils {

    private static final String NCTL_HOME = "~/Documents/casper/casper-node/utils/nctl";

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
