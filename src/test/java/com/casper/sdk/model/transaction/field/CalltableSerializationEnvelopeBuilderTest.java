package com.casper.sdk.model.transaction.field;

import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueDeserializationException;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for the {@link CalltableSerializationEnvelopeBuilder}
 *
 * @author ian@meywood.com
 */
class CalltableSerializationEnvelopeBuilderTest {

    @Test
    void serializeBytes() throws ValueSerializationException, ValueDeserializationException {

        final byte[] expected = {
                0x3, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1, 0x0, 0x1, 0x0, 0x0, 0x0, 0x2, 0x0, 0x5, 0x0, 0x0,
                0x0, 0x7, 0x0, 0x0, 0x0, (byte) 0xfe, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x2b, 0x2
        };

        final byte[] fieldZeroVal = new byte[]{(byte) 254};

        SerializerBuffer ser = new SerializerBuffer();
        ser.writeU32(4294967295L);
        final byte[] fieldOneVal = ser.toByteArray();

        ser = new SerializerBuffer();
        ser.writeU16((short) 555);
        final byte[] fieldTwoVal = ser.toByteArray();

        final CalltableSerializationEnvelopeBuilder builder = new CalltableSerializationEnvelopeBuilder();
        builder.setExpectedFields(3);
        builder.addFieldBytes(0, fieldZeroVal);
        builder.addFieldBytes(1, fieldOneVal);
        builder.addFieldBytes(2, fieldTwoVal);

        ser = new SerializerBuffer();
        builder.serialize(ser);
        byte[] bytes = ser.toByteArray();

        assertNotNull(bytes);
        assertThat(bytes.length, is(33));
        assertThat(bytes, is(expected));

        final CalltableSerializationEnvelopeBuilder deserializedBuilder = new CalltableSerializationEnvelopeBuilder();
        deserializedBuilder.deserialize(new DeserializerBuffer(bytes));

        assertThat(deserializedBuilder.getFieldBytes(0), is(fieldZeroVal));
        assertThat(deserializedBuilder.getFieldBytes(1), is(fieldOneVal));
        assertThat(deserializedBuilder.getFieldBytes(2), is(fieldTwoVal));
    }

    @Test
    void serializeValues() throws ValueSerializationException, ValueDeserializationException {

        final byte[] expected = {
                0x3, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1, 0x0, 0x1, 0x0, 0x0, 0x0, 0x2, 0x0, 0x5, 0x0, 0x0,
                0x0, 0x7, 0x0, 0x0, 0x0, (byte) 0xfe, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x2b, 0x2
        };

        final byte fieldZeroVal = (byte) 254;
        final long fieldOneVal = 4294967295L;
        final short fieldTwoVal = 555;

        final CalltableSerializationEnvelopeBuilder builder = new CalltableSerializationEnvelopeBuilder();
        builder.setExpectedFields(3);
        builder.addField(0, fieldZeroVal);
        builder.addField(1, (int) fieldOneVal);
        builder.addField(2, fieldTwoVal);

        SerializerBuffer ser = new SerializerBuffer();
        builder.serialize(ser);
        byte[] bytes = ser.toByteArray();

        assertNotNull(bytes);
        assertThat(bytes.length, is(33));
        assertThat(bytes, is(expected));

        final CalltableSerializationEnvelopeBuilder deserializedBuilder = new CalltableSerializationEnvelopeBuilder();
        deserializedBuilder.deserialize(new DeserializerBuffer(bytes));

        assertThat(deserializedBuilder.getFieldValue(0, Byte.class), is(fieldZeroVal));
        assertThat(deserializedBuilder.getFieldValue(1, Long.class), is(fieldOneVal));
        assertThat(deserializedBuilder.getFieldValue(2, Short.class), is(fieldTwoVal));
    }
}
