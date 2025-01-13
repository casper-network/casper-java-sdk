package com.casper.sdk.model.deploy;

import com.casper.sdk.exception.CasperClientException;
import com.casper.sdk.model.key.PublicKey;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.security.NoSuchAlgorithmException;

/**
 * Delegation from public key
 *
 * @author carl@stormeye.co.uk
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class DelegatorKindPublicKey implements DelegatorKind {

    @JsonProperty("PublicKey")
    private PublicKey publicKey;

    @JsonCreator
    public DelegatorKindPublicKey(final String publicKey) {
        try {
            PublicKey.fromTaggedHexString(publicKey);
        } catch (NoSuchAlgorithmException e) {
            throw new CasperClientException("Invalid public key bytes", e);
        }
    }

}
