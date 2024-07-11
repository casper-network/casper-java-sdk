package com.casper.sdk.identifier.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * The address of an addressable entity.
 *
 * @author carl@stormeye.co.uk
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class EntityAddrIdentifier implements EntityIdentifier{
    /**
     * Entity Address
     */
    @JsonProperty("EntityAddr")
    private String entityAddr;
}
