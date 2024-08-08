package com.casper.sdk.model.transaction.target;

import com.casper.sdk.jackson.resolver.TransactionTargetResolver;
import com.casper.sdk.jackson.serializer.TransactionTargetSerializer;
import com.casper.sdk.model.clvalue.serde.CasperSerializableObject;
import com.casper.sdk.model.key.Tag;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;

/**
 * The execution target of a `Transaction`.
 *
 * @author ian@meywood.com
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonTypeResolver(TransactionTargetResolver.class)
@JsonSerialize(using = TransactionTargetSerializer.class)
public interface TransactionTarget extends CasperSerializableObject, Tag {
}

