package com.casper.sdk.model.transaction.target;

import com.casper.sdk.jackson.deserializer.TransactionTargetDeserializer;
import com.casper.sdk.jackson.serializer.TransactionTargetSerializer;
import com.casper.sdk.model.clvalue.serde.CasperSerializableObject;
import com.casper.sdk.model.key.Tag;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * The execution target of a `Transaction`.
 *
 * @author ian@meywood.com
 */
@JsonSerialize(using = TransactionTargetSerializer.class)
@JsonDeserialize(using = TransactionTargetDeserializer.class)
public interface TransactionTarget extends CasperSerializableObject, Tag {}
