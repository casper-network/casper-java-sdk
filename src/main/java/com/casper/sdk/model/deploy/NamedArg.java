package com.casper.sdk.model.deploy;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.AbstractCLValue;
import com.casper.sdk.model.clvalue.cltype.AbstractCLType;
import com.casper.sdk.model.clvalue.serde.CasperSerializableObject;
import com.casper.sdk.model.clvalue.serde.Target;
import com.fasterxml.jackson.annotation.JsonFormat;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.*;

/**
 * Named arguments to a contract
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public class NamedArg<P extends AbstractCLType> implements CasperSerializableObject {

    /**
     * The first value in the array is the type of the arg
     */
    private String type;

    /**
     * The second value in the array is a CLValue type
     */
    private AbstractCLValue<?, P> clValue;

    @Override
    public void serialize(SerializerBuffer ser, Target target) throws ValueSerializationException, NoSuchTypeException {
        ser.writeString(type);
        clValue.serialize(ser, target);
    }
}
