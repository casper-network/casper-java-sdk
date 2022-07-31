package com.casper.sdk.model.storedvalue;

import com.casper.sdk.jackson.resolver.StoredValueResolver;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;

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
    T getValue();
}
