package com.casper.sdk.types;

public class ContractHash extends Digest {

    public ContractHash(final String hash) {
        super(hash);
    }

    public ContractHash(final byte[] hash) {
        super(hash);
    }
}
