package com.casper.sdk.model.deploy;

import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.casper.sdk.model.key.PublicKey;
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
 *
 * Changed 5/24
 * @author Carl Norburn
 * Refactored to match the Casper documentation
 * <a href="https://docs.casper.network/concepts/serialization-standard/#unbondingpurse"></a>
 *
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
    @JsonProperty("amount")
    private BigInteger amount;

    @JsonProperty("new_validator")
    private BigInteger newValidator;

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

}
