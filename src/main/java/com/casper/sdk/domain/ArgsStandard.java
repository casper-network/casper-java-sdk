package com.casper.sdk.domain;

public class ArgsStandard extends AbstractArgs{
    private String cl_type;

    public ArgsStandard(final String bytes, final Object parsed, final String cl_type) {
        super(bytes, parsed);
        this.cl_type = cl_type;
    }

    public String getCl_type() {
        return cl_type;
    }
}
