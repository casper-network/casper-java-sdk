package com.casper.sdk.service.http.rpc.transfers;

import java.util.List;

public class Header {

    private String account;
    private String body_hash;
    private String chain_name;
    private List<String> dependencies;
    private String gas_price;
    private String timestamp;
    private String ttl;

    public Header() {}

    public Header(final String account, final String body_hash, final String chain_name, final List<String> dependencies, final String gas_price, final String timestamp, final String ttl) {
        this.account = account;
        this.body_hash = body_hash;
        this.chain_name = chain_name;
        this.dependencies = dependencies;
        this.gas_price = gas_price;
        this.timestamp = timestamp;
        this.ttl = ttl;
    }

    public String getAccount() {
        return account;
    }

    public String getBody_hash() {
        return body_hash;
    }

    public String getChain_name() {
        return chain_name;
    }

    public List<String> getDependencies() {
        return dependencies;
    }

    public String getGas_price() {
        return gas_price;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getTtl() {
        return ttl;
    }
}
