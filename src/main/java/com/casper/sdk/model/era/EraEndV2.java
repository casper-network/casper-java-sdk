package com.casper.sdk.model.era;

import com.casper.sdk.exception.InvalidKeyBytesException;
import com.casper.sdk.model.key.PublicKey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
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
    @JsonProperty("rewards")
    private Map<PublicKey, List<BigInteger>> rewards;
    @JsonProperty("next_era_gas_price")
    private int nextEraGasPrice;

    @JsonSetter("rewards")
    public void setRewards(final Map<String, List<BigInteger>> rewards) {

        this.rewards = new LinkedHashMap<>();

        if (rewards != null) {
            rewards.forEach((key, value) -> {
                try {
                    // Jackson is not good at deserializing keys so we have to do it manually
                    this.rewards.put(PublicKey.fromTaggedHexString(key), value);
                } catch (NoSuchAlgorithmException e) {
                    throw new InvalidKeyBytesException(e);
                }
            });
        }
    }
}
