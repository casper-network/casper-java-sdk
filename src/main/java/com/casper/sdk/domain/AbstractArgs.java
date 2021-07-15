package com.casper.sdk.domain;

public abstract class AbstractArgs {

     private String bytes;
     private Object parsed;

     public AbstractArgs() {
     }

     public AbstractArgs(final String bytes, final Object parsed) {
          this.bytes = bytes;
          this.parsed = parsed;
     }

     public String getBytes() {
          return bytes;
     }

     public Object getParsed() {
          return parsed;
     }
}
