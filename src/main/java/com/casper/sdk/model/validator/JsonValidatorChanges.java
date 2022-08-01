package com.casper.sdk.model.validator;

import com.casper.sdk.model.key.PublicKey;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * The changes in a validator's status
 *
 * @author alexandre carvalho
 * @author andre bertolace
 * @since 0.2.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JsonValidatorChanges {

    /**
     * The public key of the validator
     */
    @JsonProperty("public_key")
    private PublicKey publicKey;

    /**
     * The set of changes to the validator's status.
     * @see JsonValidatorStatusChange
     */
    @JsonProperty("status_changes")
    private List<JsonValidatorStatusChange> statusChanges;

}
