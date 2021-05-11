package com.casper.sdk.domain;

import java.util.HashMap;

public class ArgsOption extends AbstractArgs {

    private HashMap<String, Object> cl_type;

    public ArgsOption(final String bytes, final Object parsed, final HashMap<String, Object> cl_type) {
        super(bytes, parsed);
        this.cl_type = cl_type;
    }

    public HashMap<String, Object> getCl_type() {
        return cl_type;
    }
}
