package com.casper.sdk.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account implements EntityAddressKind {

    @JsonProperty("Account")
    private String account;

}
