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
 * An implmentation of Transform that Adds the given `u256`
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
@JsonTypeName("AddUInt256")
public class AddUInt256 implements Transform {

    /**
     * u256
     */
    @JsonIgnore
    private BigInteger u256;

    /**
     * getter for u256 json serialization
     *
     * @return cost as expected for json serialization
     */
    @JsonProperty("AddUInt256")
    @ExcludeFromJacocoGeneratedReport
    protected String getJsonU256() {
        return this.u256.toString(10);
    }

    /**
     * setter for u256 from json deserialized value
     *
     * @param value the deserialized value
     */
    @JsonProperty("AddUInt256")
    @ExcludeFromJacocoGeneratedReport
    protected void setJsonU256(String value) {
        this.u256 = new BigInteger(value, 10);
    }
}
