package com.syntifi.casper.sdk.model.deploy.transform;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.syntifi.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;

import lombok.Data;

/**
 * An implmentation of Transform that Adds the given `u64`
 * 
 * @see Transform
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
@JsonTypeName("AddUInt64")
public class AddUInt64 implements Transform {

    /**
     * u64
     */
    @JsonIgnore
    private BigInteger u64;

    @JsonProperty("AddUInt64")
    @ExcludeFromJacocoGeneratedReport
	protected String getJsonU64() {
        return this.u64.toString(10);
    }

    @JsonProperty("AddUInt64")
    @ExcludeFromJacocoGeneratedReport
	protected void setJsonU64(String value) {
        this.u64 = new BigInteger(value, 10);
    }

}
