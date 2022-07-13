package com.casper.sdk.service.result;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NamedKey {

    /**
     * key The value of the entry: a casper `Key` type.
     */
    private final String key;

    /**
     * name The name of the entry.
     */
    private final String name;

    @JsonCreator
    public NamedKey(@JsonProperty("key") final String key, @JsonProperty("name") final String name) {
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }
}
