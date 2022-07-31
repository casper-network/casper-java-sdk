package com.casper.sdk.model.deploy.transform;

import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

/**
 * An implmentation of Transform that Adds the given `u512`
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
@JsonTypeName("AddUInt512")
public class AddUInt512 implements Transform {

    /**
     * u512
     */
    @JsonIgnore
    private BigInteger u512;

    /**
     * getter for u512 json serialization
     *
     * @return cost as expected for json serialization
     */
    @JsonProperty("AddUInt512")
    @ExcludeFromJacocoGeneratedReport
    protected String getJsonU512() {
        return this.u512.toString(10);
    }

    /**
     * setter for u512 from json deserialized value
     *
     * @param value the deserialized value
     */
    @JsonProperty("AddUInt512")
    @ExcludeFromJacocoGeneratedReport
    protected void setJsonU512(String value) {
        this.u512 = new BigInteger(value, 10);
    }
}
