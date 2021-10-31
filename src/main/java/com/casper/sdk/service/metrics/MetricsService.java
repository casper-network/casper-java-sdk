package com.casper.sdk.service.metrics;

import com.casper.sdk.service.http.rpc.HttpMethods;

public class MetricsService {

    public static final String METRICS = "metrics";
    private final HttpMethods httpMethods;

    public MetricsService(final HttpMethods httpMethods) {
        this.httpMethods = httpMethods;
    }

    public String getMetrics() {
        return httpMethods.callGetMethod(METRICS).orElse(null);
    }
}
