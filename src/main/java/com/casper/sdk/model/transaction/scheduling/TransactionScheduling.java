package com.casper.sdk.model.transaction.scheduling;

import com.casper.sdk.jackson.deserializer.TransactionSchedulingDeserializer;
import com.casper.sdk.jackson.serializer.TransactionSchedulingSerializer;
import com.casper.sdk.model.clvalue.serde.CasperSerializableObject;
import com.casper.sdk.model.key.Tag;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * The scheduling mode of a Transaction.
 *
 * @author ian@meywood.com
 */
@JsonDeserialize(using = TransactionSchedulingDeserializer.class)
@JsonSerialize(using = TransactionSchedulingSerializer.class)
public interface TransactionScheduling extends CasperSerializableObject, Tag {

}
