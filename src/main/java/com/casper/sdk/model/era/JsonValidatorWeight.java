package com.casper.sdk.model.era;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.casper.sdk.model.key.PublicKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

/**
 * Casper block validator weight
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see JsonEraEnd
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
