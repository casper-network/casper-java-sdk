package com.casper.sdk.how_to.common;

import com.casper.sdk.KeyPairStreams;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Shared methods for the How-Tos
 */
public abstract class Methods {

    private static final String NCTL_HOME = "nctl.home";
    private static final String DEFAULT_NCTL_HOME = "~/casper-node/utils/nctl";

    protected KeyPairStreams getUserKeyPairStreams(final int userNumber) throws IOException {

        final String userNPath = String.format("%s/assets/net-1/users/user-%d", getNctlHome(), userNumber);

        return new KeyPairStreams(
                new FileInputStream(new File(userNPath, "public_key.pem")),
                new FileInputStream(new File(userNPath, "secret_key.pem"))
        );
    }

    protected String getNctlHome() {
        final String nctlHome = System.getProperty(NCTL_HOME, DEFAULT_NCTL_HOME);
        // Replace user home '~' tilda with full path to user home directory
        return nctlHome.replaceFirst("^~", System.getProperty("user.home"));
    }

    protected KeyPairStreams getNodeKeyPair(final int nodeNumber) throws IOException {

        final String userNPath = String.format("%s/assets/net-1/nodes/node-%d/keys", getNctlHome(), nodeNumber);

        return new KeyPairStreams(
                new FileInputStream(new File(userNPath, "public_key.pem")),
                new FileInputStream(new File(userNPath, "secret_key.pem"))
        );
    }

    protected InputStream getWasmIn(String wasmPath) {
        return getClass().getResourceAsStream(getNctlHome() + wasmPath);
    }
}
