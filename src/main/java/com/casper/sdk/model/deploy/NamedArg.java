package com.casper.sdk.model.deploy;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.*;
import com.casper.sdk.model.clvalue.cltype.AbstractCLType;
import com.casper.sdk.model.clvalue.serde.CasperSerializableObject;
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
    public void serialize(SerializerBuffer ser, boolean encodeType) throws ValueSerializationException, NoSuchTypeException {
        ser.writeString(type);
        if (clValue instanceof CLValueI32 || clValue instanceof CLValueU32) {
            ser.writeI32(32 / 8);
        }
        if (clValue instanceof CLValueI64 || clValue instanceof CLValueU64) {
            ser.writeI32(64 / 8);
        }
        if (clValue instanceof CLValueU128 || clValue instanceof CLValueU256 ||
                clValue instanceof CLValueU512 || clValue instanceof CLValuePublicKey) {
            SerializerBuffer localEncoder = new SerializerBuffer();
            clValue.serialize(localEncoder);
            int size = localEncoder.toByteArray().length;
            ser.writeI32(size); //removing the CLValue type byte at the end
        }
        if (clValue instanceof CLValueOption) {
            SerializerBuffer localEncoder = new SerializerBuffer();
            clValue.serialize(localEncoder);
            int size = localEncoder.toByteArray().length;
            ser.writeI32(size); //removing the CLValue type byte at the end
        }
        clValue.serialize(ser, encodeType);
    }
}
