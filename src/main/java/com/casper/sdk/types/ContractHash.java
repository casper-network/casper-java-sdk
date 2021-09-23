package com.casper.sdk.types;

public class ContractHash extends Digest {

    public ContractHash(String hash) {
        super(hash);
    }

    public ContractHash(byte[] hash) {
        super(hash);
    }
}
