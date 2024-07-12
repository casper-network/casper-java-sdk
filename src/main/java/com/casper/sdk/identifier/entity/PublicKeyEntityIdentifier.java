package com.casper.sdk.identifier.entity;

import com.casper.sdk.model.key.PublicKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * The public key of an account.
 *
 * @author carl@stormeye.co.uk
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class PublicKeyEntityIdentifier implements EntityIdentifier {

    /** Public key hash */
    @JsonProperty("PublicKey")
    private PublicKey publicKey;

}
