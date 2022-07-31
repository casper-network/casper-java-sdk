package com.casper.sdk.model.deploy;

import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.casper.sdk.model.key.PublicKey;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.casper.sdk.model.uref.URef;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

/**
 * Unbonding Purse
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UnbondingPurse {

    /**
     * Unbonding amount
     */
    @JsonIgnore
    private BigInteger unbondingAmount;

    /**
     * the bondingPurse's {@link URef}
     */
    @JsonProperty("bonding_purse")
    private URef bondingPurse;

    /**
     * Era in which this unbonding request was created.
     */
    @JsonProperty("era_of_creation")
    private BigInteger eraOfCreation;

    /**
     * Unbonders {@link PublicKey}
     */
    @JsonProperty("unbonder_public_key")
    private PublicKey unbonderPublicKey;

    /**
     * Validators {@link PublicKey}
     */
    @JsonProperty("validator_public_key")
    private PublicKey validatorPublicKey;

    /**
     * getter for unbondingAmount json serialization
     *
     * @return cost as expected for json serialization
     */
    @JsonProperty("unbonding_amount")
    @ExcludeFromJacocoGeneratedReport
    protected String getJsonUnbondingAmount() {
        return this.unbondingAmount.toString(10);
    }

    /**
     * setter for unbondingAmount from json deserialized value
     *
     * @param value the deserialized value
     */
    @JsonProperty("unbonding_amount")
    @ExcludeFromJacocoGeneratedReport
    protected void setJsonUnbondingAmount(String value) {
        this.unbondingAmount = new BigInteger(value, 10);
    }
}
