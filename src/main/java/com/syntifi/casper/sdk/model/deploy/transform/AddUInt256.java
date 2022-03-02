package com.syntifi.casper.sdk.model.deploy.transform;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.syntifi.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * An implmentation of Transform that Adds the given `u256`
 * 
 * @see Transform
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@JsonTypeName("AddUInt256")
public class AddUInt256 implements Transform {

    /**
     * u256
     */
    @JsonIgnore
    private BigInteger u256;

    @JsonProperty("AddUInt256")
    @ExcludeFromJacocoGeneratedReport
    protected String getJsonU256() {
        return this.u256.toString(10);
    }

    @JsonProperty("AddUInt256")
    @ExcludeFromJacocoGeneratedReport
    protected void setJsonU256(String value) {
        this.u256 = new BigInteger(value, 10);
    }
}
