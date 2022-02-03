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
                return ByteUtils.concat(
                        typesFactory.getInstance(CLType.U32).serialize(((CLMap) toSerialize).size()),
                        buildMapBytes((CLMap)toSerialize)
                );

            } else {
                // The map has not been modified so write as is
                return ((CLMap) toSerialize).getBytes();
            }
        } else {
            return new byte[0];
        }
    }

    private byte[] buildMapBytes(final CLMap clMap) {
        final ByteArrayBuilder builder = new ByteArrayBuilder();

        for (Map.Entry<CLValue, CLValue> entry : clMap.entrySet()) {
            builder.append(entry.getKey().getBytes());
            builder.append(entry.getValue().getBytes());
        }
        return builder.toByteArray();
    }
}
