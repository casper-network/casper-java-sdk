package com.casper.sdk.service.http.rpc;

import java.util.Collections;
import java.util.Map;

/**
 * Simple class to build up the rpc json call The class is instantiated and it's json conversion used as the rpc
 * payload
 */
public class Method {

    private static final String JSON_RPC = "2.0";
    private static final int id = 1;
    private final String method;
    /** The parameters that will be written as JSON */
    private final Map<String, Object> params;

    public Method(final String method) {
        this(method, Collections.emptyMap());
    }

    public Method(final String method, final Map<String, Object> params) {
        this.method = method;
        this.params = params;
    }

    public int getId() {
        return id;
    }

    public String getJsonrpc() {
        return JSON_RPC;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    @Override
    public String toString() {
        return "Method{" +
               "method='" + method + '\'' +
               ", params=" + params +
               '}';
    }
}
