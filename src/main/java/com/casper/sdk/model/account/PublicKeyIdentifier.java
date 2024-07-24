package com.casper.sdk.model.account;

import com.casper.sdk.exception.CasperClientException;
import com.casper.sdk.model.key.PublicKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.security.NoSuchAlgorithmException;

/**
 * The public key of an account.
 *
 * @author carl@stormeye.co.uk
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class PublicKeyIdentifier implements AccountIdentifier {

    /** Public key hash */
    @JsonProperty("PublicKey")
    private PublicKey publicKey;

    public PublicKeyIdentifier(final String hexKey) {
        try {
            this.publicKey = PublicKey.fromTaggedHexString(hexKey);
        } catch (NoSuchAlgorithmException e) {
            throw new CasperClientException("Invalid public key bytes", e);
        }
    }
}
