package com.syntifi.crypto.key.hash;

import org.bouncycastle.jcajce.provider.digest.Keccak;

public class Keccak256 {

    public static byte[] digest(byte[] value) {
        Keccak.DigestKeccak kekkac256 = new Keccak.Digest256();
        byte[] hash = kekkac256.digest(value);
        return hash;
    }
}
