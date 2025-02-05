package com.casper.sdk.model.transaction;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.CLValuePublicKey;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.transaction.field.CalltableSerializationEnvelopeBuilder;
import com.fasterxml.jackson.annotation.JsonCreator;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.NoArgsConstructor;

/**
 * The public key of the account that initiated the contract call.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
public class InitiatorPublicKey extends InitiatorAddr<PublicKey> {

    @JsonCreator
    public InitiatorPublicKey(final PublicKey address) {
        super(address);
    }

    @Override
    public byte getByteTag() {
        return PUBLIC_KEY_TAG;
    }

    @Override
    public void serialize(final SerializerBuffer ser, final Target target) throws ValueSerializationException, NoSuchTypeException {
        new CalltableSerializationEnvelopeBuilder()
                .addField(TAG_FIELD_INDEX,  /* U8 */  getByteTag())
                .addField(ADDR_KEY_FIELD_INDEX, new CLValuePublicKey(getAddress()))
                .serialize(ser, target);
    }
}
