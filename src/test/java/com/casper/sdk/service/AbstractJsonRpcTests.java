
package com.casper.sdk.service;

import com.casper.sdk.model.AbstractJsonTests;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.BeforeAll;

import java.net.MalformedURLException;

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
        MAIN_NET("65.109.54.159", 7777),
        TEST_NET("85.114.132.133", 7777),
        NCTL("127.0.0.1", 11101),
        NCTL_SPECULATIVE("127.0.0.1", 25101);

        private final String ip;
        private final int port;
    }

    protected static CasperService casperServiceMainnet;
    protected static CasperService casperServiceTestnet;
    protected static CasperService casperServiceNctl;
    protected static CasperService speculativeCasperServiceNctl;

    @BeforeAll
    public static void setUp() throws MalformedURLException {
        casperServiceMainnet = CasperService.usingPeer(CasperNetwork.MAIN_NET.getIp(),
                CasperNetwork.MAIN_NET.getPort());
        casperServiceTestnet = CasperService.usingPeer(CasperNetwork.TEST_NET.getIp(),
                CasperNetwork.TEST_NET.getPort());
        casperServiceNctl = CasperService.usingPeer(CasperNetwork.NCTL.getIp(),
                CasperNetwork.NCTL.getPort());
        speculativeCasperServiceNctl = CasperService.usingPeer(CasperNetwork.NCTL_SPECULATIVE.getIp(),
                CasperNetwork.NCTL_SPECULATIVE.getPort());
    }
}
