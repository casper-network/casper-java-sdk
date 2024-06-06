package com.casper.sdk.model.block;

import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.era.EraEndV1;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Holds the header data of a Casper block
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see BlockV1
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JsonBlockHeader extends BlockHeader {

      /**
     * @see EraEndV1
     */
    @JsonProperty("era_end")
    private EraEndV1 eraEnd;
}
