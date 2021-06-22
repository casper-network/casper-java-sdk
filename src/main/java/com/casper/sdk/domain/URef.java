package com.casper.sdk.domain;

import com.casper.sdk.service.serialization.util.ByteUtils;

public class URef {

    private final byte[] bytes;
    private final AccessRights accessRights;

    public URef(final byte[] bytes, final AccessRights accessRights) {
        this.bytes = bytes;
        this.accessRights = accessRights;
    }

    public URef(String hex, AccessRights accessRights) {
        this(ByteUtils.decodeHex(hex), accessRights);

    }

    public byte[] getBytes() {
        return bytes;
    }

    public AccessRights getAccessRights() {
        return accessRights != null ? accessRights : AccessRights.NONE;
    }
}
