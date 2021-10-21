package com.syntifi.casper.sdk.model.transfer;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.syntifi.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;

import lombok.Data;

/**
 * Represents a transfer from one purse to another
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
public class Transfer {
    @JsonProperty("id")
    private Long id;

    /**
     * Hex-encoded account hash.
     */
    @JsonProperty("to")
    private String to;

    /**
     * Hex-encoded account hash.
     */
    @JsonProperty("from")
    private String from;

    /**
     * Amount transfered
     */
    @JsonIgnore
    private BigInteger amount;

    /**
     * Hex-encoded hash
     */
    @JsonProperty("deploy_hash")
    private String deployHash;

    /**
     * Hex-encoded, formatted URef
     */
    @JsonProperty("source")
    private String source;

    /**
     * Hex-encoded, formatted URef
     */
    @JsonProperty("target")
    private String target;

    /**
     * Decimal representation of a 512-bit integer.
     */
    @JsonIgnore
    private BigInteger gas;

    @JsonProperty("amount")
    @ExcludeFromJacocoGeneratedReport
	protected String getJsonAmount() {
        return this.amount.toString(10);
    }

    @JsonProperty("amount")
    @ExcludeFromJacocoGeneratedReport
	protected void setJsonAmount(String value) {
        this.amount = new BigInteger(value, 10);
    }

    @JsonProperty("gas")
    @ExcludeFromJacocoGeneratedReport
	protected String getJsonGas() {
        return this.gas.toString(10);
    }

    @JsonProperty("gas")
    @ExcludeFromJacocoGeneratedReport
	protected void setJsonGas(String value) {
        this.gas = new BigInteger(value, 10);
    }
}
