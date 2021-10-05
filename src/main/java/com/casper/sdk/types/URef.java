package com.casper.sdk.types;

import com.casper.sdk.service.serialization.util.ByteUtils;

import java.util.regex.Pattern;

public class URef {

    private final byte[] bytes;
    private final AccessRights accessRights;

    public URef(final byte[] bytes, final AccessRights accessRights) {
        this.bytes = bytes;
        this.accessRights = accessRights;
    }

    public URef(final String uRef) {
        this(getBytes(uRef), getAccessRights(uRef));
    }

    public URef(final String hex, final AccessRights accessRights) {
        this(ByteUtils.decodeHex(hex), accessRights);
    }

    private static byte[] getBytes(final String uRef) {
        final String[] split = uRef.split(Pattern.quote("-"));
        if ("uref".equals(split[0])) {
            return ByteUtils.decodeHex(split[1]);
        } else {
            throw new IllegalArgumentException("invalid URef " + uRef);
        }
    }

    private static AccessRights getAccessRights(final String uRef) {
        return AccessRights.valueOf(Character.getNumericValue(uRef.charAt(uRef.length() - 1)));
    }

    public byte[] getBytes() {
        return bytes;
    }

    public AccessRights getAccessRights() {
        return accessRights != null ? accessRights : AccessRights.NONE;
    }


}
