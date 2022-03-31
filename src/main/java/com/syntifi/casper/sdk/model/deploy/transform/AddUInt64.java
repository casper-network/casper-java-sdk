package com.syntifi.casper.sdk.model.deploy.transform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.syntifi.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

/**
 * An implmentation of Transform that Adds the given `u64`
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see Transform
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeName("AddUInt64")
public class AddUInt64 implements Transform {

    /**
     * u64
     */
    @JsonIgnore
    private BigInteger u64;

    /**
     * getter for u64 json serialization
     *
     * @return cost as expected for json serialization
     */
    @JsonProperty("AddUInt64")
    @ExcludeFromJacocoGeneratedReport
    protected String getJsonU64() {
        return this.u64.toString(10);
    }

    /**
     * setter for u64 from json deserialized value
     *
     * @param value the deserialized value
     */
    @JsonProperty("AddUInt64")
    @ExcludeFromJacocoGeneratedReport
    protected void setJsonU64(String value) {
        this.u64 = new BigInteger(value, 10);
    }
}
