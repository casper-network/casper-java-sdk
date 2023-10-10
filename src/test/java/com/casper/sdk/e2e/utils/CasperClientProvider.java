package com.casper.sdk.e2e.utils;

import com.casper.sdk.service.CasperService;
import com.casper.sdk.service.EventService;
import com.casper.sdk.e2e.exception.TestException;
import lombok.Getter;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Provides a singleton instance of the casper client services.
 *
 * @author ian@meywood.com
 */
@Getter
public class CasperClientProvider {

    private static CasperClientProvider instance;
    private final CasperService casperService;
    private final EventService eventService;
    private final CasperService speculauaCasperService;

    public static synchronized CasperClientProvider getInstance() {
        if (instance == null) {
            instance = new CasperClientProvider();
        }
        return instance;
    }

    private CasperClientProvider() {

        try {
            final TestProperties properties = new TestProperties();
            casperService = CasperService.usingPeer(properties.getHostname(), properties.getRcpPort());
            speculauaCasperService = CasperService.usingPeer(properties.getHostname(), properties.getSpxPort());
            //noinspection HttpUrlsUsage
            eventService = EventService.usingPeer(new URI("http://" + properties.getHostname() + ":" + properties.getSsePort()));
        } catch (MalformedURLException | URISyntaxException e) {
            throw new TestException(e);
        }
    }

}
