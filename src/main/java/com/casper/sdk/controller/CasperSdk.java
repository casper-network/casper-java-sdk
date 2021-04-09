package com.casper.sdk.controller;

import org.apache.commons.codec.DecoderException;
import com.casper.sdk.Properties;
import com.casper.sdk.service.HashService;
import com.casper.sdk.service.QueryService;

import java.security.NoSuchAlgorithmException;

/**
 * Entry point into the SDK
 * Exposes all permissible methods
 */
public class CasperSdk {

    private final QueryService queryService;

    public CasperSdk(final String url, final String port) {
        Properties.properties.put("node-url", url);
        Properties.properties.put("node-port", port);
        this.queryService = new QueryService();
    }

    public String getAccountInfo(final String accountKey) throws Throwable {
        return queryService.getAccountInfo(accountKey);
    }

    public String getAccountBalance(final String accountKey) throws Throwable {
        return queryService.getAccountBalance(accountKey);
    }

    public String getAccountMainPurseURef(final String accountKey) throws Throwable {
        return queryService.getAccountMainPurseURef(accountKey);
    }

    public String getStateRootHash() throws Throwable {
        return queryService.getStateRootHash();
    }

    public String getAccountHash(final String accountKey) throws DecoderException, NoSuchAlgorithmException {
        return HashService.getAccountHash(accountKey);
    }

}
