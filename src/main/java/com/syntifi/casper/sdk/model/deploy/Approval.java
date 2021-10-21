package com.syntifi.casper.sdk.model.deploy;

import com.syntifi.casper.sdk.model.key.PublicKey;
import com.syntifi.casper.sdk.model.key.Signature;

import lombok.Data;

/**
 * A struct containing a signature and the public key of the signer
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
public class Approval {

    /**
     * @see PublicKey
     */
    private PublicKey signer;

    /**
     * @see Signature
     */
    private Signature signature;
}
