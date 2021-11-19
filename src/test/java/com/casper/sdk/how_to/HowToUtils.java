package com.casper.sdk.how_to;

import com.casper.sdk.KeyPairStreams;

import java.io.*;

/**
 * Utility methods for the How-Tos
 */
final class HowToUtils {

    private static final String NCTL_HOME = "nctl.home";
    private static final String DEFAULT_NCTL_HOME = "~/casper-node/utils/nctl";

    static KeyPairStreams getUserKeyPairStreams(final int userNumber) throws IOException {

        final String userNPath = String.format("%s/assets/net-1/users/user-%d", getNctlHome(), userNumber);

        return new KeyPairStreams(
                new FileInputStream(new File(userNPath, "public_key.pem")),
                new FileInputStream(new File(userNPath, "secret_key.pem"))
        );
    }

    static String getNctlHome() {
        final String nctlHome = System.getProperty(NCTL_HOME, DEFAULT_NCTL_HOME);
        // Replace user home '~' tilda with full path to user home directory
        return nctlHome.replaceFirst("^~", System.getProperty("user.home"));
    }

    static KeyPairStreams getNodeKeyPair(final int nodeNumber) throws IOException {

        final String userNPath = String.format("%s/assets/net-1/nodes/node-%d/keys", getNctlHome(), nodeNumber);

        return new KeyPairStreams(
                new FileInputStream(new File(userNPath, "public_key.pem")),
                new FileInputStream(new File(userNPath, "secret_key.pem"))
        );
    }

    static KeyPairStreams getFaucetKeyPair() throws FileNotFoundException {
        return new KeyPairStreams(
                new FileInputStream(new File(getNctlHome() + "/faucet", "public_key.pem")),
                new FileInputStream(new File(getNctlHome() + "/faucet", "secret_key.pem"))
        );

    }

    static InputStream getWasmIn(String wasmPath) {
        return HowToUtils.class.getResourceAsStream(getNctlHome() + wasmPath);
    }
}
