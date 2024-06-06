package com.casper.sdk.model.block;

import com.casper.sdk.model.era.ValidatorWeight;
import com.casper.sdk.model.key.PublicKey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EraEndV2 {

    private List<PublicKey> equivocators;
    @JsonProperty("inactive_validators")
    private List<PublicKey> inactiveValidators;
    @JsonProperty("next_era_validator_weights")
    private List<ValidatorWeight> nextEraValidatorWeights;
    // TODO - Fix deserialization to allow key to be PublicKey
    @JsonProperty("rewards")
    private Map<String, List<BigInteger>> rewards;
    @JsonProperty("next_era_gas_price")
    private int nextEraGasPrice;
}
