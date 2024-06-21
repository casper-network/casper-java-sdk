package com.casper.sdk.model.deploy.transform;

import com.casper.sdk.model.transfer.TransferV1;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * An implementation of Transform that Writes the given Transfer to global state.
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
@JsonTypeName("WriteTransfer")
public class WriteTransfer implements Transform {

    /**
     * @see TransferV1
     */
    @JsonProperty("WriteTransfer")
    private TransferV1 transfer;
}
