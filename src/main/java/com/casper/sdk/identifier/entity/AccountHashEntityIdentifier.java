package com.casper.sdk.identifier.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * The account hash of an account.
 *
 * @author carl@stormeye.co.uk
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class AccountHashEntityIdentifier implements EntityIdentifier{

    /**
     * Account hash
     */
    @JsonProperty("AccountHash")
    private String accountHash;

}
