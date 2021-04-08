package com.casper.sdk.service.http.rpc;

import java.util.HashMap;

public class Method {

    private final int id = 1;
    private final String jsonrpc = "2.0";
    private String method;
    private HashMap<String, Object> params;

    public Method() {}

    public Method(final String method) {
        this.method = method;
    }

    public Method(final String method, final HashMap<String, Object> params) {
        this.method = method;
        this.params = params;
    }

}
