package com.casper.sdk.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Package associated with an Account hash.
 *
 * @author carl@stormeye.co.uk
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account implements EntityAddressKind {

    @JsonProperty("Account")
    private String account;

}
