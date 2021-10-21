package com.syntifi.casper.sdk.model.deploy.transform;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.syntifi.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;

import lombok.Data;

/**
 * An implmentation of Transform that Adds the given `u128`
 * 
 * @see Transform
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
@JsonTypeName("AddUInt128")
public class AddUInt128 implements Transform {

    /**
     * u128
     */
    @JsonIgnore
    private BigInteger u128;

    @JsonProperty("AddUInt128")
    @ExcludeFromJacocoGeneratedReport
	protected String getJsonU128() {
        return this.u128.toString(10);
    }

    @JsonProperty("AddUInt128")
    @ExcludeFromJacocoGeneratedReport
	protected void setJsonU128(String value) {
        this.u128 = new BigInteger(value, 10);
    }
}
