package com.syntifi.casper.sdk.crypto;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class PublicKey {
    private byte[] key;

    public abstract void readPublicKey(String filename) throws IOException;

    public abstract void writePublicKey(String filename) throws IOException;

    public abstract Boolean verify(String message, String hexSignature);
}
