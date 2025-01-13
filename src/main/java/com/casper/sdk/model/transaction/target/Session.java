package com.casper.sdk.model.transaction.target;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.jackson.deserializer.TransactionTargetDeserializer;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.transaction.field.CalltableSerializationEnvelopeBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The execution target is the included module bytes, i.e. compiled Wasm.
 * <p>
 * Note this class is serialized using the @see TransactionTargetSerializer and deserialized using the
 *
 * @author ian@meywood.com
 * @see TransactionTargetDeserializer not the default Jackson serializer/deserializer.
 */
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@JsonTypeName("Session")
@JsonPropertyOrder({"installUpgrade", "runtime", "moduleBytes"})
public class Session implements TransactionTarget {

    private final int TAG_FIELD_INDEX = 0;
    private final byte SESSION_TAG = 2;
    private final int SESSION_IS_INSTALL_INDEX = 1;
    private final int SESSION_RUNTIME_INDEX = 2;
    private final int SESSION_MODULE_BYTES_INDEX = 3;
    private final int SESSION_TRANSFERRED_VALUE_INDEX = 4;
    private final int SESSION_SEED_INDEX = 5;

    /** Flag determining if the Wasm is an install/upgrade. */
    @JsonProperty("is_install_upgrade")
    private boolean installUpgrade;
    /** The execution runtime to use. */
    @JsonProperty("runtime")
    private TransactionRuntime runtime;
    /** The compiled Wasm. */
    @JsonProperty("module_bytes")
    private byte[] moduleBytes;
    @JsonProperty("transferred_value")
    private long transferredValue;
    @JsonProperty("seed")
    private Digest seed;

    @Override
    public void serialize(final SerializerBuffer ser, final Target target) throws ValueSerializationException, NoSuchTypeException {

        new CalltableSerializationEnvelopeBuilder()
                .addField(TAG_FIELD_INDEX, getByteTag())
                .addField(SESSION_IS_INSTALL_INDEX, isInstallUpgrade())
                .addField(SESSION_RUNTIME_INDEX, getRuntime())
                .addField(SESSION_MODULE_BYTES_INDEX, getModuleBytes())
                .addField(SESSION_TRANSFERRED_VALUE_INDEX, getTransferredValue())
                .addOptionField(SESSION_SEED_INDEX,  getSeed())
                .serialize(ser, target);
    }

    @Override
    @JsonIgnore
    public byte getByteTag() {
        return SESSION_TAG;
    }
}
