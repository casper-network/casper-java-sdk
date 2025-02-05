package com.casper.sdk.model.transaction.target;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.transaction.field.CalltableSerializationEnvelopeBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.*;

import java.util.Optional;

/**
 * The address and optional version identifying the package.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ByPackageName implements TransactionInvocationTarget {

    private static final int TAG_FIELD_INDEX = 0;
    private static final int BY_PACKAGE_NAME_NAME_INDEX = 1;
    private final static int BY_PACKAGE_NAME_VERSION_INDEX = 2;

    /** the package name */
    private String name;
    /** If `None`, the latest enabled version is implied. */
    @Getter(AccessLevel.NONE)
    @JsonProperty("version")
    private Long version;

    @JsonIgnore
    public Optional<Long> getVersion() {
        return Optional.ofNullable(version);
    }

    @Override
    public void serialize(final SerializerBuffer ser, final Target target) throws ValueSerializationException, NoSuchTypeException {
        new CalltableSerializationEnvelopeBuilder()
                .addField(TAG_FIELD_INDEX, getByteTag())
                .addField(BY_PACKAGE_NAME_NAME_INDEX, name)
                .addOptionField(BY_PACKAGE_NAME_VERSION_INDEX, version)
                .serialize(ser, target);
    }

    @JsonIgnore
    @Override
    public byte getByteTag() {
        return 3;
    }
}
