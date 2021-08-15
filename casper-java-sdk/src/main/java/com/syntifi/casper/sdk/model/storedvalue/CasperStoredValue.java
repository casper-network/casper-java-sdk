package com.syntifi.casper.sdk.model.storedvalue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.syntifi.casper.sdk.jackson.CasperStoredValueDeserializer;

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
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(using = CasperStoredValueDeserializer.class)
public class CasperStoredValue<T> {
    @JsonProperty("cl_type")
    private String clType;
    private byte[] bytes;
    private T parsed;
}
