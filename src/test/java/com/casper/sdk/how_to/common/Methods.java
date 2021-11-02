package com.casper.sdk.how_to.common;

import com.casper.sdk.KeyPairStreams;

import java.io.*;

/**
 * Shared methods for the How-Tos
 */
public abstract class Methods {


    protected KeyPairStreams getUserKeyPairStreams(final int userNumber, final String defaultNctlHome) throws IOException {

        final String userNPath = String.format("%s/assets/net-1/users/user-%d", getNctlHome(defaultNctlHome), userNumber);

        return new KeyPairStreams(
                new FileInputStream(new File(userNPath, "public_key.pem")),
                new FileInputStream(new File(userNPath, "secret_key.pem"))
        );
    }

    protected String getNctlHome(final String defaultNctlHome) {
        String nctlHome = System.getProperty("nctl.home");
        if (nctlHome == null) {
            nctlHome = defaultNctlHome;
        }
        // Replace user home '~' tilda with full path to user home directory
        nctlHome = nctlHome.replaceFirst("^~", System.getProperty("user.home"));
        return nctlHome;
    }

    protected KeyPairStreams getNodeKeyPair(final int nodeNumber, final String defaultNctlHome) throws IOException {

        final String userNPath = String.format("%s/assets/net-1/nodes/node-%d/keys", getNctlHome(defaultNctlHome), nodeNumber);

        return new KeyPairStreams(
                new FileInputStream(new File(userNPath, "public_key.pem")),
                new FileInputStream(new File(userNPath, "secret_key.pem"))
        );
    }

}
