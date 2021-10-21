package com.syntifi.casper.sdk.model.deploy.transform;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.syntifi.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;

import lombok.Data;

/**
 * An implmentation of Transform that Adds the given `u512`
 * 
 * @see Transform
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
@JsonTypeName("AddUInt512")
public class AddUInt512 implements Transform {

    /**
     * u512
     */
    @JsonIgnore
    private BigInteger u512;

    @JsonProperty("AddUInt512")
    @ExcludeFromJacocoGeneratedReport
	protected String getJsonU512() {
        return this.u512.toString(10);
    }

    @JsonProperty("AddUInt512")
    @ExcludeFromJacocoGeneratedReport
	protected void setJsonU512(String value) {
        this.u512 = new BigInteger(value, 10);
    }
}
