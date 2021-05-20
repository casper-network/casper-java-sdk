package com.casper.sdk.domain;

import java.util.Objects;

abstract class AbstractCLType {

    private final CLTypeInfo typeInfo;

    protected AbstractCLType(final CLTypeInfo typeInfo) {
        Objects.requireNonNull(typeInfo, "type cannot be null");
        this.typeInfo = typeInfo;
    }

    public CLTypeInfo getCLTypeInfo() {
        return typeInfo;
    }

    public String toHex() {
        return toHex(getBytes());
    }

    public abstract byte[] getBytes();

    public static byte[] fromString(final String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    public static String toHex(final byte[] bytes) {

        final StringBuilder hexBuilder = new StringBuilder();
        for (byte b : bytes) {
            hexBuilder.append(String.format("%02x", b));
        }

        return hexBuilder.toString();
    }
}
