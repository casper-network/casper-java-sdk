package com.casper.sdk.model.deploy.transform;

import com.casper.sdk.model.bid.Bid;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * An implementation of Transform that Writes the given Bid to global state.
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
@JsonTypeName("WriteBid")
public class WriteBid implements Transform {

    /**
     * @see Bid
     */
    @JsonProperty("WriteBid")
    private Bid bid;
}
