package com.casper.sdk.model.transaction;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.AbstractCLValue;
import com.casper.sdk.model.clvalue.CLValueOption;
import com.casper.sdk.model.clvalue.CLValueU64;
import com.casper.sdk.model.clvalue.serde.CasperSerializableObject;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.deploy.NamedArg;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Named arguments for a transaction.
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@Getter
public class NamedArgs implements Iterable<NamedArg<?>>, CasperSerializableObject {

    public static final String ID = "id";

    @JsonProperty("Named")
    private final List<NamedArg<?>> args = new ArrayList<>();

    public NamedArgs(final List<NamedArg<?>> args) {
        if (args != null) {
            this.args.addAll(args);
        }
    }

    public NamedArgs(final NamedArg<?>... args) {
        if (args != null) {
            for (NamedArg<?> arg : args) {
                add(arg);
            }
        }
    }

    public NamedArgs add(final NamedArg<?> namedArg) {
        args.add(namedArg);
        return this;

    }


    public NamedArg<?> get(final String name) {
        for (NamedArg<?> arg : args) {
            if (name.equals(arg.getType())) {
                return arg;
            }
        }
        return null;
    }

    @NotNull
    @Override
    public Iterator<NamedArg<?>> iterator() {
        return args.iterator();
    }

    public NamedArg<?> get(final int index) {
        return args.get(index);
    }

    @Override
    public void serialize(SerializerBuffer ser, Target target) throws ValueSerializationException, NoSuchTypeException {
        ser.writeI32(args.size());

        for (NamedArg<?> namedArg : args) {
            if (ID.equals(namedArg.getType())) {
                validateId(namedArg.getClValue());
            }
            namedArg.serialize(ser, target);
        }
    }

    private void validateId(final AbstractCLValue<?, ?> idNamedArgValue) {
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
