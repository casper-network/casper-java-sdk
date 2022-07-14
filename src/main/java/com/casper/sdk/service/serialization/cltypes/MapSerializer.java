package com.casper.sdk.service.serialization.cltypes;

import com.casper.sdk.service.serialization.util.ByteArrayBuilder;
import com.casper.sdk.service.serialization.util.ByteUtils;
import com.casper.sdk.types.CLMap;
import com.casper.sdk.types.CLType;
import com.casper.sdk.types.CLValue;

import java.util.Map;

import static com.casper.sdk.service.serialization.util.ByteUtils.lastNBytes;

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

            // The map is mutable to the bytes need to be reserialized if modified or not yet created
            if (isModifiedOrNotYetSerialized((CLMap) toSerialize)) {
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

    private boolean isModifiedOrNotYetSerialized(final CLMap toSerialize) {
        return toSerialize.isModified() || toSerialize.getBytes() == null;
    }

    private byte[] serializeMap(final Map<CLValue, CLValue> toSerialize) {
        byte[] length = typesFactory.getInstance(CLType.U32).serialize(toSerialize.size());
        byte[] map = buildKeyValueBytes(toSerialize);
        return ByteUtils.concat(
                length,
                map
        );
    }

    private byte[] buildKeyValueBytes(final Map<CLValue, CLValue> clMap) {

        final ByteArrayBuilder builder = new ByteArrayBuilder();

        for (Map.Entry<CLValue, CLValue> entry : clMap.entrySet()) {
            builder.append(removeLength(entry.getKey()));
            builder.append(removeLength(entry.getValue()));
        }
        return builder.toByteArray();
    }

    private byte[] removeLength(final CLValue value) {

        if (value.getCLType() == CLType.BYTE_ARRAY) {
            return removeLength(value.getBytes());
        } else {
            return value.getBytes();
        }
    }

    private byte[] removeLength(byte[] bytes) {
        if (bytes != null && bytes.length > 3) {
            return lastNBytes(bytes, bytes.length - 4);
        } else {
            return bytes;
        }
    }
}
