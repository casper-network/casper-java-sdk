package com.casper.sdk.model.transaction.field;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.CasperSerializableObject;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.transaction.NamedArgs;
import com.casper.sdk.model.transaction.entrypoint.TransactionEntryPoint;
import com.casper.sdk.model.transaction.scheduling.TransactionScheduling;
import com.casper.sdk.model.transaction.target.TransactionTarget;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The fields of a transaction written as a short indices and byte values.
 *
 * @author ian@meywood.com
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Fields implements CasperSerializableObject {

    private static final int ARGS_MAP_KEY = 0;
    private static final int TARGET_MAP_KEY = 1;
    private static final int ENTRY_POINT_MAP_KEY = 2;
    private static final int SCHEDULING_MAP_KEY = 3;

    @JsonProperty("args")
    private NamedArgs args;
    @JsonProperty("target")
    private TransactionTarget target;
    @JsonProperty("entry_point")
    private TransactionEntryPoint entryPoint;
    @JsonProperty("scheduling")
    private TransactionScheduling scheduling;

    @Override
    public void serialize(final SerializerBuffer ser, final Target target) throws ValueSerializationException, NoSuchTypeException {

        final Map<Integer, CasperSerializableObject> fields = new LinkedHashMap<>();

        if (this.args == null) {
            this.args = new NamedArgs();
        }

        fields.put(ARGS_MAP_KEY, this.args);
        fields.put(TARGET_MAP_KEY, this.target);
        fields.put(ENTRY_POINT_MAP_KEY, this.entryPoint);
        fields.put(SCHEDULING_MAP_KEY, this.scheduling);

        ser.writeI32(fields.size());

        for (final Map.Entry<Integer, CasperSerializableObject> entry : fields.entrySet()) {
            ser.writeU16(entry.getKey().shortValue());
            final SerializerBuffer valueBuf = new SerializerBuffer();
            entry.getValue().serialize(valueBuf, target);
            final byte[] valueBytes = valueBuf.toByteArray();
            ser.writeI32(valueBytes.length);
            ser.writeByteArray(valueBytes);
        }
    }
}
