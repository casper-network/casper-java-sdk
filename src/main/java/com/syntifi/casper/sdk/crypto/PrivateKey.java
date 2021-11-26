package com.syntifi.casper.sdk.crypto;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class PrivateKey {
    private byte[] key;

    public abstract void readPrivateKey(String filename) throws IOException;

    public abstract String sign(String msg);

    public abstract String verify(String msg);

    public abstract PublicKey derivePublicKey();
}
