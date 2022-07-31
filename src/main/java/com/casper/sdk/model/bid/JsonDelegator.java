package com.casper.sdk.model.bid;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.uref.URef;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

/**
 * A delegator associated with the give validator
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
public class JsonDelegator {

    /**
     * @see URef
     */
    @JsonProperty("bonding_purse")
    private URef bondingPurse;

    /**
     * @see PublicKey
     */
    private PublicKey delegatee;

    /**
     * @see PublicKey
     */
    @JsonProperty("public_key")
    private PublicKey publicKey;

    /**
     * amount
     */
    @JsonIgnore
    private BigInteger stakedAmount;

    @JsonProperty("staked_amount")
    @ExcludeFromJacocoGeneratedReport
    protected String getJsonStakedAmount() {
        return this.stakedAmount.toString(10);
    }

    @JsonProperty("staked_amount")
    @ExcludeFromJacocoGeneratedReport
    protected void setJsonStakedAmount(String value) {
        this.stakedAmount = new BigInteger(value, 10);
    }
}
