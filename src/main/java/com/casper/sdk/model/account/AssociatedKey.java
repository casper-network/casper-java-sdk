package com.casper.sdk.model.account;

import com.casper.sdk.model.key.AccountHashKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Associated Key
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
public class AssociatedKey {

    /**
     * account_hash(String) Hex-encoded account hash.
     */
    @JsonProperty("account_hash")
    private AccountHashKey accountHash;

    /**
     * weight(Integer)
     */
    @JsonProperty("weight")
    private int weight;
}
