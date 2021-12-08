package com.syntifi.casper.sdk.crypto;

import java.io.IOException;
import java.security.GeneralSecurityException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractPrivateKey {
    private byte[] key;

    public abstract void readPrivateKey(String filename) throws IOException;

    public abstract void writePrivateKey(String filename) throws IOException;

    public abstract String sign(String message) throws GeneralSecurityException;

    public abstract AbstractPublicKey derivePublicKey();
}
