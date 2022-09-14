package com.casper.sdk.types;

import com.casper.sdk.service.serialization.cltypes.CLValueBuilder;
import com.casper.sdk.service.serialization.types.ByteSerializerFactory;
import com.casper.sdk.service.serialization.util.ByteUtils;
import com.casper.sdk.service.serialization.util.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * Unit tests for a CLMap value
 */
class CLMapTest {

    private CLValue key1;
    private CLValue value1;
    private CLValue key2;
    private CLValue value2;
    private CLMap clMap;

    private final ByteSerializerFactory serializerFactory = new ByteSerializerFactory();

    @BeforeEach
    void setUp() {

        key1 = CLValueBuilder.string("key1");
        value1 = CLValueBuilder.i32(1234);

        key2 = CLValueBuilder.string("key2");
        value2 = CLValueBuilder.i32(5678);

        clMap = new CLMap((String) null, new CLMapTypeInfo(key1.getCLTypeInfo(), value1.getCLTypeInfo()), null);

        clMap.put(key1, value1);
        clMap.put(key2, value2);

    }

    @Test
    void clMap() {

        clMap.clear();

        assertThat(clMap.isEmpty(), is(true));
        assertThat(clMap.iterator().hasNext(), is(false));

        clMap.put(key1, value1);

        assertThat(clMap.isEmpty(), is(false));
        assertThat(clMap.iterator().hasNext(), is(true));

        clMap.put(key2, value2);

        assertThat(clMap.getCLType(), is(CLType.MAP));

        assertThat(clMap.getKeyType(), is(new CLTypeInfo(CLType.STRING)));
        assertThat(clMap.getValueType(), is(new CLTypeInfo(CLType.I32)));

        assertThat(clMap.get(key1), is(value1));
        assertThat(clMap.get(key2), is(value2));

        assertThat(clMap.size(), is(2));

        assertThat(clMap.containsKey(key1), is(true));
        assertThat(clMap.containsKey(CLValueBuilder.string("key3")), is(false));

        assertThat(clMap.containsValue(value1), is(true));
        assertThat(clMap.containsValue(CLValueBuilder.i32(999)), is(false));
    }

    @Test
    void clMapEntrySet() {

        final Iterator<Map.Entry<CLValue, CLValue>> iterator = clMap.entrySet().iterator();
        assertThat(iterator.hasNext(), is(true));

        Map.Entry<CLValue, CLValue> next = iterator.next();
        assertThat(next.getKey(), is(key1));
        assertThat(next.getValue(), is(value1));

        assertThat(iterator.hasNext(), is(true));

        next = iterator.next();
        assertThat(next.getKey(), is(key2));
        assertThat(next.getValue(), is(value2));

        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    void clMapValues() {
        assertThat(clMap.values(), hasItems(value1, value2));
    }

    @Test
    void clMapKeys() {
        assertThat(clMap.keySet(), hasItems(key1, key2));
    }

    @Test
    void clMapRemove() {
        assertThat(clMap.remove(key1), is(value1));
        assertThat(clMap.get(key1), is(nullValue()));
    }

    @Test
    void clMapPutAll() {

        clMap.clear();
        assertThat(clMap.size(), is(0));
        assertThat(clMap.isEmpty(), is(true));

        clMap.putAll(CollectionUtils.Map.of(key1, value1, key2, value2));

        assertThat(clMap.getKeyType(), is(key1.getCLTypeInfo()));
        assertThat(clMap.getValueType(), is(value2.getCLTypeInfo()));

        assertThat(clMap.size(), is(2));
    }

    @Test
    void clMapIsModified() {

        final CLMap map = new CLMap(
                (String) null,
                new CLMapTypeInfo(key1.getCLTypeInfo(), value1.getCLTypeInfo()),
                CollectionUtils.Map.of(key1, value1)
        );

        assertThat(map.isModified(), is(false));

        map.put(key2, value2);
        assertThat(map.isModified(), is(true));

        map.setModified(false);
        assertThat(map.isModified(), is(false));
    }

    @Test
    void clMapByteValues() {

        final String hexKey = "e3D394334Ce46C6043BCd33E4686D2B7a369C606BfCce4C26ca14d2C73Fac824";

        final CLMap clMap = new CLMap((byte[]) null,
                new CLMapTypeInfo(new CLByteArrayInfo(32), new CLTypeInfo(CLType.U256)),
                CollectionUtils.Map.of(
                        new CLValue(ByteUtils.decodeHex(hexKey), new CLByteArrayInfo(32), hexKey),
                        CLValueBuilder.u256("400000")
                )
        );

        final CLValue key1 = clMap.keySet().iterator().next();
        assertThat(key1.getBytes(), is(ByteUtils.decodeHex("e3D394334Ce46C6043BCd33E4686D2B7a369C606BfCce4C26ca14d2C73Fac824")));
        assertThat(key1.getParsed(), is("e3D394334Ce46C6043BCd33E4686D2B7a369C606BfCce4C26ca14d2C73Fac824"));

        final CLValue value1 = clMap.values().iterator().next();
        assertThat(value1.getParsed(), is("400000"));
        assertThat(value1.getBytes(), is(ByteUtils.decodeHex("03801a06")));
    }

    @Test
    void clMapByteSerializationTest() {

        byte[] keyBytes = ByteUtils.decodeHex("e07cA98F1b5C15bC9ce75e8adB8a3b4D334A1B1Fa14DD16CfD3320bf77Cc3aAb");
        byte[] rawBytes = {-32, 124, -87, -113, 27, 92, 21, -68, -100, -25, 94, -118, -37, -118, 59, 77, 51, 74, 27, 31, -95, 77, -47, 108, -3, 51, 32, -65, 119, -52, 58, -85};
        assertThat(keyBytes, is(rawBytes));

        final CLValue clKey = CLValueBuilder.byteArray(keyBytes);
        final CLValue clValue = CLValueBuilder.u256(0.4e6);

        byte[] expectedKeyBytes = {
                32, 0, 0, 0, (byte) 224, 124, (byte) 169, (byte) 143, 27, 92, 21, (byte) 188, (byte) 156, (byte) 231,
                94, (byte) 138, (byte) 219, (byte) 138, 59, 77, 51, 74, 27, 31, (byte) 161, 77, (byte) 209, 108,
                (byte) 253, 51, 32, (byte) 191, 119, (byte) 204, 58, (byte) 171, 15, 32, 0, 0, 0
        };
        //assertThat(clKey.getBytes(), is(expectedKeyBytes));

        byte[] expectedValueBytes = {3, (byte) 128, 26, 6};
        assertThat(clValue.getBytes(), is(expectedValueBytes));

        byte[] expectedValueBytesWithType = {4, 0, 0, 0, 3, (byte) 128, 26, 6, 7};
        byte[] clValueBytes = serializerFactory.getByteSerializer(clValue).toBytes(clValue);
        assertThat(clValueBytes, is(expectedValueBytesWithType));


        byte[] clKeyBytes = serializerFactory.getByteSerializer(clKey).toBytes(clKey);
        assertThat(clKeyBytes, is(expectedKeyBytes));

        final CLMap clMap = CLValueBuilder.map(CollectionUtils.Map.of(clKey, clValue));
        byte[] clMapBytes = serializerFactory.getByteSerializer(clMap).toBytes(clMap);

        // The expected bytes for the serialized CLMap
        byte[] expectedClMapBytes = {
                40, 0, 0, 0, // length of CLMap bytes from this point onwards..... (whole byte array written as toBytesArrayU8(bytes)
                1, 0, 0, 0, // number of key-value pairs in the map
                (byte) 224, 124, (byte) 169, (byte) 143, 27, 92, 21, (byte) 188, (byte) 156,
                (byte) 231, 94, (byte) 138, (byte) 219, (byte) 138, 59, 77, 51, 74, 27, 31, (byte) 161, 77, (byte) 209,
                108, (byte) 253, 51, 32, (byte) 191, 119, (byte) 204, 58, (byte) 171, // Key bytes
                3, (byte) 128, 26, 6, // value bytes
                17, // CLMap type
                15, // key type - Byte array
                32, 0, 0, 0, // Byte array length U32
                7  // value type U256
        };

        assertThat(clMapBytes, is(expectedClMapBytes));
    }
}