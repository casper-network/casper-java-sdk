package com.casper.sdk.service.result;

class AbstractResult {

    private final String apiVersion;

    AbstractResult(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getApiVersion() {
        return apiVersion;
    }

}
