package com.casper.sdk.model.transaction;

import com.casper.sdk.jackson.resolver.KindResolver;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;

/**
 * The kind of an Effect.
 *
 * @author ian@meywood.com
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonTypeResolver(KindResolver.class)
public interface Kind {
}
