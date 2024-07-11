package com.casper.sdk.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Package associated with a native contract implementation.
 *
 * @author carl@stormeye.co.uk
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class System implements EntityAddressKind {

    @JsonCreator
    public static System create(final SystemEntityType value){
        return new System(value);
    }

    public enum SystemEntityType {
        // Mint contract.
        @JsonProperty("Mint")
        MINT,
        // Handle Payment contract.
        @JsonProperty("HandlePayment")
        HANDLE_PAYMENT,
        // Standard Payment contract.
        @JsonProperty("StandardPayment")
        STANDARD_PAYMENT,
        // Auction contract.
        @JsonProperty("Auction")
        AUCTION
    }


    @JsonProperty("System")
    private SystemEntityType system;

}
