package com.casper.sdk.service.serialization.cltypes;

import com.casper.sdk.service.serialization.util.ByteArrayBuilder;
import com.casper.sdk.service.serialization.util.ByteUtils;
import com.casper.sdk.types.CLMap;
import com.casper.sdk.types.CLType;
import com.casper.sdk.types.CLValue;

import java.util.Map;

/**
 * The serializer for a Map
 */
public class MapSerializer implements TypesSerializer {

    final TypesFactory typesFactory;

    public MapSerializer(final TypesFactory typesFactory) {
        this.typesFactory = typesFactory;
    }

    @Override
    public byte[] serialize(final Object toSerialize) {

        if (toSerialize instanceof CLMap) {

            if (((CLMap) toSerialize).isModified() || ((CLMap) toSerialize).getBytes() == null) {
                ((CLMap) toSerialize).setModified(false);
                //noinspection unchecked
                return serializeMap((Map<CLValue, CLValue>) toSerialize);

            } else {
                // The map is not new and has not been modified so write as is
                return ((CLMap) toSerialize).getBytes();
            }
        } else if (toSerialize instanceof Map) {
            //noinspection unchecked
            return serializeMap((Map<CLValue, CLValue>) toSerialize);
        } else {
            return new byte[0];
        }
    }

    private byte[] serializeMap(final Map<CLValue, CLValue> toSerialize) {
        return ByteUtils.concat(
                typesFactory.getInstance(CLType.U32).serialize(toSerialize.size()),
                buildKeyValueBytes(toSerialize)
        );
    }

    private byte[] buildKeyValueBytes(final Map<CLValue, CLValue> clMap) {

        final ByteArrayBuilder builder = new ByteArrayBuilder();

        for (Map.Entry<CLValue, CLValue> entry : clMap.entrySet()) {
            builder.append(entry.getKey().getBytes());
            builder.append(entry.getValue().getBytes());
        }
        return builder.toByteArray();
    }
}
