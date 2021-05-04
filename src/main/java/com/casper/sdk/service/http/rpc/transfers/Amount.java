package com.casper.sdk.service.http.rpc.transfers;

public class Amount {

    private String bytes;
    private String cl_type;
    private String parsed;

    public Amount() {}

    public Amount(final String bytes, final String cl_type, final String parsed) {
        this.bytes = bytes;
        this.cl_type = cl_type;
        this.parsed = parsed;
    }

    public String getBytes() {
        return bytes;
    }

    public String getCl_type() {
        return cl_type;
    }

    public String getParsed() {
        return parsed;
    }
}
