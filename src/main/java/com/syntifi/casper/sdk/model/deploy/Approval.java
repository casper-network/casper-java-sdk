package com.syntifi.casper.sdk.model.deploy;

import java.io.IOException;

import com.syntifi.casper.sdk.exception.CLValueEncodeException;
import com.syntifi.casper.sdk.exception.DynamicInstanceException;
import com.syntifi.casper.sdk.exception.NoSuchTypeException;
import com.syntifi.casper.sdk.model.clvalue.encdec.CLValueEncoder;
import com.syntifi.casper.sdk.model.clvalue.encdec.interfaces.EncodableValue;
import com.syntifi.casper.sdk.model.key.PublicKey;
import com.syntifi.casper.sdk.model.key.Signature;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A struct containing a signature and the public key of the signer
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Approval implements EncodableValue {

    /**
     * @see PublicKey
     */
    private PublicKey signer;

    /**
     * @see Signature
     */
    private Signature signature;

    /**
     * Implements Approval encoder 
     */
    @Override
    public void encode(CLValueEncoder clve, boolean encodeType) throws IOException, CLValueEncodeException, DynamicInstanceException, NoSuchTypeException  {
        signer.encode(clve, encodeType);
        signature.encode(clve, encodeType);
    }
}
