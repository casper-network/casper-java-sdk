package com.casper.sdk.service;

import java.net.MalformedURLException;

import com.casper.sdk.model.AbstractJsonTests;

import org.junit.jupiter.api.BeforeAll;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Abstract class for testing json rpc methods
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public abstract class AbstractJsonRpcTests extends AbstractJsonTests {
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public enum CasperNetwork {
        MAIN_NET("63.33.251.206", 7777), TEST_NET("139.180.189.141", 7777);

        private final String ip;
        private final int port;
    }

    protected static CasperService casperServiceMainnet;
    protected static CasperService casperServiceTestnet;

    @BeforeAll
    public static void setUp() throws MalformedURLException {
        casperServiceMainnet = CasperService.usingPeer(CasperNetwork.MAIN_NET.getIp(),
                CasperNetwork.MAIN_NET.getPort());
        casperServiceTestnet = CasperService.usingPeer(CasperNetwork.TEST_NET.getIp(),
                CasperNetwork.TEST_NET.getPort());
    }
}
