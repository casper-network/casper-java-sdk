package com.casper.sdk.identifier.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

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
