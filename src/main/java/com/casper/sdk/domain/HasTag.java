package com.casper.sdk.domain;

public interface HasTag {
    /**
     * Obtains the byte tag for this type of casper object
     *
     * @return the tag number
     */
    int getTag();
}
