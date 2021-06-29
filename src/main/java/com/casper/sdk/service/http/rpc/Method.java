package com.casper.sdk.service.http.rpc;

import java.util.Map;

/**
 * Simple class to build up the rpc json call The class is instantiated and it's json conversion used as the rpc
 * payload
 */
public class Method {

    private static final String jsonrpc = "2.0";
    private final int id = 1;
    private String method;
    private Map<String, Object> params;

    public Method() {
    }

    public Method(final String method) {
        this.method = method;
    }

    public Method(final String method, final Map<String, Object> params) {
        this.method = method;
        this.params = params;
    }

    public int getId() {
        return id;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, Object> getParams() {
        return params;
    }
}
