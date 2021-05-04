package com.casper.sdk.service;

import java.util.ArrayList;
import java.util.List;

public enum SerializationTypes {

    Bool{
        @Override public String getValue(final String result) {
            return null;
        }
    },
    I32{
        @Override public java.lang.String getValue(final java.lang.String result) {
            return null;
        }
    },
    I64{
        @Override public java.lang.String getValue(final java.lang.String result) {
            return null;
        }
    },
    U8{
        @Override public java.lang.String getValue(final java.lang.String result) {
            return null;
        }
    },
    U32{
        @Override public java.lang.String getValue(final java.lang.String result) {
            return null;
        }
    },
    U128{
        @Override public java.lang.String getValue(final java.lang.String result) {
            return null;
        }
    },
    U256{
        @Override public java.lang.String getValue(final java.lang.String result) {
            return null;
        }
    },
    U512{
        @Override public java.lang.String getValue(final java.lang.String result) {
            return null;
        }
    },
    Unit{
        @Override public java.lang.String getValue(final java.lang.String result) {
            return null;
        }
    },
    String{
        @Override public java.lang.String getValue(final java.lang.String result) {
            return null;
        }
    },
    URef{
        @Override public java.lang.String getValue(final java.lang.String result) {
            return null;
        }
    },
    Key{
        @Override public java.lang.String getValue(final java.lang.String result) {
            return null;
        }
    },
    Option{
        @Override public java.lang.String getValue(final java.lang.String result) {
            return null;
        }
    },
    List{
        @Override public java.lang.String getValue(final java.lang.String result) {
            return null;
        }
    },
    FixedList{
        @Override public java.lang.String getValue(final java.lang.String result) {
            return null;
        }
    },
    Result{
        @Override public java.lang.String getValue(final java.lang.String result) {
            return null;
        }
    },
    Map{
        @Override public java.lang.String getValue(final java.lang.String result) {
            return null;
        }
    },
    Tuple1{
        @Override public java.lang.String getValue(final java.lang.String result) {
            return null;
        }
    },
    Tuple2{
        @Override public java.lang.String getValue(final java.lang.String result) {
            return null;
        }
    },
    Tuple3{
        @Override public java.lang.String getValue(final java.lang.String result) {
            return null;
        }
    },
    Any{
        @Override public java.lang.String getValue(final java.lang.String result) {
            return null;
        }
    };




    private static final List<String> MAP = new ArrayList<>();
    public abstract String getValue(final String result) ;

    static {
        for (final SerializationTypes value : values()) {
            MAP.add(value.name());
        }
    }



}
