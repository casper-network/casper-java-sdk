package com.casper.sdk.model.deploy;

import com.casper.sdk.model.key.PublicKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * @author carl@stormeye.co.uk
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode()
public class DelegatorKindPublicKey implements DelegatorKind {

    @JsonProperty("PublicKey")
    private PublicKey publicKey;

}
