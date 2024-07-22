package com.casper.sdk.model.transfer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A transfer of tokens from one account to another
 *
 * @author ian@meywood.com
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transfer {

    @JsonProperty("Version1")
    private TransferV1 transferV1;

    @JsonProperty("Version2")
    private TransferV2 transferV2;
}
