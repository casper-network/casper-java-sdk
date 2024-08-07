package com.casper.sdk.model.transaction;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.AbstractCLValue;
import com.casper.sdk.model.clvalue.CLValueOption;
import com.casper.sdk.model.clvalue.CLValueU64;
import com.casper.sdk.model.clvalue.serde.CasperSerializableObject;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.deploy.NamedArg;
import com.casper.sdk.model.transaction.entrypoint.TransactionEntryPoint;
import com.casper.sdk.model.transaction.scheduling.TransactionScheduling;
import com.casper.sdk.model.transaction.target.TransactionTarget;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.*;

import java.util.List;

/**
 * The body of a transaction.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonPropertyOrder({"args", "target", "entry_point", "transaction_category", "scheduling"})
public class TransactionV1Body implements CasperSerializableObject {
    private List<NamedArg<?>> args;
    private TransactionTarget target;
    @JsonProperty("entry_point")
    private TransactionEntryPoint entryPoint;
    @JsonProperty("transaction_category")
    private TransactionCategory transactionCategory;
    private TransactionScheduling scheduling;

    @Override
    public void serialize(final SerializerBuffer ser, final Target target) throws ValueSerializationException, NoSuchTypeException {

        serializeNamedArgs(ser, target);
        this.target.serialize(ser, target);
        this.entryPoint.serialize(ser, target);
        this.transactionCategory.serialize(ser, target);
        this.scheduling.serialize(ser, target);
    }

    void serializeNamedArgs(final SerializerBuffer ser, final Target target) throws ValueSerializationException, NoSuchTypeException {
        ser.writeI32(getArgs().size());

        for (NamedArg<?> namedArg : getArgs()) {
            if ("id".equals(namedArg.getType())) {
                validateId(namedArg.getClValue());
            }
            namedArg.serialize(ser, target);
        }
    }

    public Digest buildHash() throws NoSuchTypeException, ValueSerializationException {
        final SerializerBuffer serializerBuffer = new SerializerBuffer();
        this.serialize(serializerBuffer, Target.BYTE);
        return Digest.blake2bDigestFromBytes(serializerBuffer.toByteArray());
    }

    private static void validateId(final AbstractCLValue<?,?> idNamedArgValue) {
        if (!(idNamedArgValue instanceof CLValueOption)) {
            throw new IllegalArgumentException("NamedArg type 'id' must be of type CLValueOption");
        } else {
            final CLValueOption id = (CLValueOption) idNamedArgValue;
            if (id.getValue().isPresent() && !(id.getValue().get() instanceof CLValueU64)) {
                throw new IllegalArgumentException("NamedArg type 'id' must be of type CLValueOption(CLValueU64)");
            }
        }
    }
}
