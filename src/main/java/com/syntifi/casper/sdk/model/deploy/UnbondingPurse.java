package com.syntifi.casper.sdk.model.deploy;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.syntifi.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.syntifi.casper.sdk.model.key.PublicKey;
import com.syntifi.casper.sdk.model.uref.URef;

import lombok.Data;

/**
 * Unbonding Purse
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
public class UnbondingPurse {

    /**
     * Unbonding amount
     */
    @JsonIgnore
    private BigInteger unbondingAmount;

    /**
     * @see Uref
     */
    @JsonProperty("bonding_purse")
    private URef bondingPurse;

    /**
     * Era in which this unbonding request was created.
     */
    @JsonProperty("era_of_creation")
    private BigInteger eraOfCreation;

    /**
     * Unbonders public key
     * 
     * @see PublickKey
     */
    @JsonProperty("unbonder_public_key")
    private PublicKey unbonderPublicKey;

    /**
     * Validators public key
     * 
     * @see PublickKey
     */
    @JsonProperty("validator_public_key")
    private PublicKey validatorPublicKey;

    @JsonProperty("unbonding_amount")
    @ExcludeFromJacocoGeneratedReport
	protected String getJsonUnbondingAmount() {
        return this.unbondingAmount.toString(10);
    }

    @JsonProperty("unbonding_amount")
    @ExcludeFromJacocoGeneratedReport
	protected void setJsonUnbondingAmount(String value) {
        this.unbondingAmount = new BigInteger(value, 10);
    }
}
