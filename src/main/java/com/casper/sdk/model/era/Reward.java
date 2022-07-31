package com.casper.sdk.model.era;

import com.casper.sdk.model.key.PublicKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Casper block era reward data
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see JsonEraReport
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reward {

    /**
     * @see PublicKey
     */
    private PublicKey validator;

    /**
     * amount
     */
    private long amount;
}
