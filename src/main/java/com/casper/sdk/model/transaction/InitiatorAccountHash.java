package com.casper.sdk.model.transaction;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.key.AccountHashKey;
import com.casper.sdk.model.transaction.field.CalltableSerializationEnvelopeBuilder;
import com.fasterxml.jackson.annotation.JsonCreator;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.NoArgsConstructor;

/**
 * The InitiatorAddr for an account hash.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor(force = true)
public class InitiatorAccountHash extends InitiatorAddr<AccountHashKey> {


    @JsonCreator
    public InitiatorAccountHash(final AccountHashKey address) {
        super(address);
    }

    @Override
    public byte getByteTag() {
        return ACCOUNT_HASH_TAG;
    }

    @Override
    public void serialize(final SerializerBuffer ser, final Target target) throws ValueSerializationException, NoSuchTypeException {
        new CalltableSerializationEnvelopeBuilder(target)
                .addField(TAG_FIELD_INDEX,  /* U8 */  getByteTag())
                .addField(ADDR_KEY_FIELD_INDEX, getAddress())
                .serialize(ser, target);
    }
}
