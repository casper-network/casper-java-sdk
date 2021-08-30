package com.syntifi.casper.sdk.model.storedvalue;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.syntifi.casper.sdk.exception.NotImplementedException;
import com.syntifi.casper.sdk.jackson.CasperStoredValueDeserializer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Casper Typed Stored Value
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see CasperStoredValueData
 * @since 0.0.1
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonDeserialize(using = CasperStoredValueDeserializer.class)
public class CasperStoredValue<T> {
    private enum CLType {
        BOOL, I32, I64, U8, U32, U64, U128, U256, U512, UNIT, STRING, // Basic data types
        KEY, UREF, OPTIONAL, LIST, BYTEARRAY, RESULT, MAP, TUPLE1, TUPLE2, TUPLE3, PUBLICKEY
    }

    @JsonProperty("cl_type")
    private String clType;
    private byte[] bytes;
    private T parsed;

    public static CasperStoredValue<? extends Object> fromValue(String clType, byte[] bytes, JsonNode toBeParsed) {
        switch (clType.toUpperCase()) {
            case "Bool":
                return new CasperStoredValue<>(clType, bytes, toBeParsed.asBoolean());
            case "I32":
                return new CasperStoredValue<>(clType, bytes, toBeParsed.asInt());
            case "I64":
                return new CasperStoredValue<>(clType, bytes, toBeParsed.asLong());
            case "U8":
            case "U32":
                return new CasperStoredValue<>(clType, bytes, toBeParsed.asInt());
            case "U64":
                return new CasperStoredValue<>(clType, bytes, toBeParsed.asLong());
            case "U128":
            case "U256":
            case "U512":
                return new CasperStoredValue<>(clType, bytes, new BigInteger(toBeParsed.asText()));
            case "Unit":
                throw new NotImplementedException();
            case "String":
                return new CasperStoredValue<>(clType, bytes, toBeParsed.asText());
            case "Key":
                throw new NotImplementedException();
            case "URef":
                throw new NotImplementedException();
            case "Optional":
                throw new NotImplementedException();
            case "List":
                throw new NotImplementedException();
            case "ByteArray":
                return new CasperStoredValue<>(clType, bytes, toBeParsed.asText().getBytes());
            case "Result":
                throw new NotImplementedException();
            case "Map":
                throw new NotImplementedException();
            case "Tuple1":
                throw new NotImplementedException();
            case "Tuple2":
                throw new NotImplementedException();
            case "Tuple3":
                throw new NotImplementedException();
            case "PublicKey":
                throw new NotImplementedException();
            default: // Any
                return new CasperStoredValue<>(clType, bytes, (Object) toBeParsed.asText());
        }
    }
}
