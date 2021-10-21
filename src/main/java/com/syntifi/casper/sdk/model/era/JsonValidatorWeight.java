package com.syntifi.casper.sdk.model.era;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.syntifi.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.syntifi.casper.sdk.model.key.PublicKey;

import lombok.Data;

/**
 * Casper block validator weight
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see JsonEraEnd
 * @since 0.0.1
 */
@Data
public class JsonValidatorWeight {

    /**
     * @see PublicKey
     */
    @JsonProperty("public_key")
    private PublicKey publicKey;

    /**
     * Decimal representation of a 512-bit integer
     */
    @JsonIgnore
    private BigInteger weight;

    @JsonProperty("weight")
    @ExcludeFromJacocoGeneratedReport
    protected String getJsonWeight() {
        return this.weight.toString(10);
    }

    @JsonProperty("weight")
    @ExcludeFromJacocoGeneratedReport
    protected void setJsonWeight(String value) {
        this.weight = new BigInteger(value, 10);
    }
}
