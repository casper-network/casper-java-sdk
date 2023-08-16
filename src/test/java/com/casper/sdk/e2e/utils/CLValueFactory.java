package com.casper.sdk.e2e.utils;

import com.casper.sdk.model.clvalue.*;
import com.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.casper.sdk.model.key.Key;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.uref.URef;
import com.casper.sdk.model.uref.URefAccessRight;
import com.syntifi.crypto.key.encdec.Hex;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.javatuples.Unit;

import java.math.BigInteger;
import java.util.*;

/**
 * @author ian@meywood.com
 */
public class CLValueFactory {

    public AbstractCLValue<?, ?> createValue(final CLTypeData clTypeData, final String strValue) throws Exception {

        switch (clTypeData) {
            case STRING:
                return new CLValueString(strValue);

            case BOOL:
                return new CLValueBool(Boolean.TRUE.toString().equalsIgnoreCase(strValue));

            case U8:
                return new CLValueU8(Byte.valueOf(strValue));

            case U32:
                return new CLValueU32(Long.valueOf(strValue));

            case U64:
                return new CLValueU64(new BigInteger(strValue));

            case U256:
                return new CLValueU256(new BigInteger(strValue));

            case I32:
                return new CLValueI32(Integer.valueOf(strValue));

            case I64:
                return new CLValueI64(Long.valueOf(strValue));

            case BYTE_ARRAY:
                return new CLValueByteArray(Hex.decode(strValue));

            case KEY:
                return new CLValueKey(Key.fromTaggedHexString(strValue));

            case PUBLIC_KEY:
                return new CLValuePublicKey(PublicKey.fromTaggedHexString(strValue));

            case UREF:
                return new CLValueURef(new URef(Hex.decode(strValue), URefAccessRight.READ_ADD_WRITE));

            default:
                throw new ValueSerializationException("Not a supported type: " + clTypeData);
        }
    }


    public AbstractCLValue<?, ?> createComplexValue(final CLTypeData clTypeData,
                                                    final Iterable<CLTypeData> innerTypes,
                                                    final Iterable<String> innerStrValues) throws Exception {

        final Iterator<String> iterStrValues = innerStrValues.iterator();
        final List<AbstractCLValue<?, ?>> innerValues = new ArrayList<>();

        for (CLTypeData innerType : innerTypes) {
            innerValues.add(this.createValue(innerType, iterStrValues.next()));
        }

        switch (clTypeData) {
            case LIST:
                return new CLValueList(innerValues);

            case MAP:
                //noinspection unchecked
                return new CLValueMap(buildMap(innerValues));

            case OPTION:
                return new CLValueOption(Optional.of(innerValues.get(0)));

            case TUPLE1:
                return new CLValueTuple1(Unit.with(innerValues.get(0)));

            case TUPLE2:
                return new CLValueTuple2(Pair.fromIterable(innerValues));

            case TUPLE3:
                return new CLValueTuple3(Triplet.fromIterable(innerValues));

            default:
                throw new ValueSerializationException("Not a supported type: " + clTypeData);
        }
    }

    @SuppressWarnings("rawtypes")
    private Map buildMap(final List<AbstractCLValue<?, ?>> innerValues) throws Exception {

        //noinspection rawtypes
        final Map<AbstractCLValue, AbstractCLValue> map = new LinkedHashMap<>();

        int i = 0;
        for (AbstractCLValue<?, ?> innerValue: innerValues) {
            AbstractCLValue<?, ?> key = new CLValueString(Integer.toString(i++));
            map.put(key, innerValue);
        }
        return  map;
    }
}
