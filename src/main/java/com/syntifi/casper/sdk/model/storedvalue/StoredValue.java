package com.syntifi.casper.sdk.model.storedvalue;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import com.syntifi.casper.sdk.jackson.resolver.StoredValueResolver;

/**
 * Stored Value interface and jackson resolver for subtypes
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonTypeResolver(StoredValueResolver.class)
public interface StoredValue<T> {
    public T getValue();
}
