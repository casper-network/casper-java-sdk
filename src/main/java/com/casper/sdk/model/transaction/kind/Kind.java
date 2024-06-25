package com.casper.sdk.model.transaction.kind;

import com.casper.sdk.jackson.resolver.KindResolver;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;

/**
 * The kind of Effect.
 *
 * @author ian@meywood.com
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonTypeResolver(KindResolver.class)
public interface Kind {
}
