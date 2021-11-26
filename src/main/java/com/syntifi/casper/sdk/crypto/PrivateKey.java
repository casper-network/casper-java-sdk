package com.syntifi.casper.sdk.crypto;

import java.io.IOException;

import org.web3j.abi.datatypes.Bool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class PrivateKey {
    private byte[] key;

    public abstract void readPrivateKey(String filename) throws IOException;

    public abstract void writePrivateKey(String filename) throws IOException;

    public abstract String sign(String msg);

    public abstract Bool verify(String msg);

    public abstract PublicKey derivePublicKey();
}
