package com.casper.sdk.model.transaction.target;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.Target;
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
        ser.writeU8(getByteTag());
        ser.writeString(name);
        if (getVersion().isPresent()) {
            ser.writeBool(true);
            ser.writeU32(version);
        } else {
            ser.writeBool(false);
        }
    }

    @Override
    public byte getByteTag() {
        return 3;
    }
}
