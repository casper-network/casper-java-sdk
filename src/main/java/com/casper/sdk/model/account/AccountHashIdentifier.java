package com.casper.sdk.model.account;

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
public class AccountHashIdentifier implements AccountIdentifier {

    /** Account hash */
    @JsonProperty("AccountHash")
    private String accountHash;

}
