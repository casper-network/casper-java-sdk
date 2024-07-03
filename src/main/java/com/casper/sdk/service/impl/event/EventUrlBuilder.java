package com.casper.sdk.service.impl.event;

import com.casper.sdk.exception.CasperClientException;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * Class for building event URLs
 */
final class EventUrlBuilder {

    /**
     * The root path to the events stream
     */
    private static final String PATH = "/events";
    /**
     * The start_from parameter that allow the specification of which ID to start from
     */
    public static final String START_FROM = "start_from";

    /**
     * Builds an event URL
     *
     * @param uri       the URI to the host
     * @param startFrom the optional 'start_from' URL query parameter will be omitted if null
     * @return the URL for the requested parameters
     */
    URL buildUrl(final URI uri, final Long startFrom) {

        try {
            return new URL(uri.toString() + PATH + buildParams(startFrom));
        } catch (MalformedURLException e) {
            throw new CasperClientException("Error building URL for " + uri, e);
        }
    }

    private String buildParams(final Long startFrom) {
        if (startFrom != null) {
            return '?' + START_FROM + '=' + startFrom;
        } else {
            return "";
        }
    }
}
