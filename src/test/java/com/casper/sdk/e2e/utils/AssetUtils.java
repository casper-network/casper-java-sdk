package com.casper.sdk.e2e.utils;

import java.net.URL;
import java.util.Objects;

/**
 * Utility class for obtaining resources from the assets folder
 *
 * @author ian@meywood.com
 */
public class AssetUtils {

    public static URL getUserKeyAsset(final int networkId, final int userId, final String keyFilename) {
        String path = String.format("/net-%d/user-%d/%s", networkId, userId, keyFilename);
        return Objects.requireNonNull(AssetUtils.class.getResource(path), "missing resource " + path);
    }

    public static URL getFaucetAsset(final int networkId, final String keyFilename) {
        String path = String.format("/net-%d/faucet/%s", networkId, keyFilename);
        return Objects.requireNonNull(AssetUtils.class.getResource(path), "missing resource " + path);
    }
}
