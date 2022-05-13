package com.casper.sdk.types;

import com.casper.sdk.service.serialization.cltypes.CLValueBuilder;
import com.casper.sdk.service.serialization.util.ByteUtils;
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

        clMap.putAll(Map.of(key1, value1, key2, value2));

        assertThat(clMap.getKeyType(), is(key1.getCLTypeInfo()));
        assertThat(clMap.getValueType(), is(value2.getCLTypeInfo()));

        assertThat(clMap.size(), is(2));
    }

    @Test
    void clMapIsModified() {

        final CLMap map = new CLMap(
                (String) null,
                new CLMapTypeInfo(key1.getCLTypeInfo(), value1.getCLTypeInfo()),
                Map.of(key1, value1)
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
                Map.of(
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
}