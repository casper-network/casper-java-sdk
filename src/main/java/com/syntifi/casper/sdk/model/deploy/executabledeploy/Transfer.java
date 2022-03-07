package com.syntifi.casper.sdk.model.deploy.executabledeploy;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.syntifi.casper.sdk.exception.CLValueEncodeException;
import com.syntifi.casper.sdk.exception.DynamicInstanceException;
import com.syntifi.casper.sdk.exception.NoSuchTypeException;
import com.syntifi.casper.sdk.model.clvalue.encdec.CLValueEncoder;
import com.syntifi.casper.sdk.model.deploy.NamedArg;

import org.bouncycastle.util.encoders.Hex;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * An AbstractExecutableDeployItem of Type Transfer containing the runtime args
 * of the contract.
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("Transfer")
public class Transfer implements ExecutableDeployItem {

    /**
     * List of @see NamedArg
     */
 
     private List<NamedArg<?>> args;

    /**
     * @link ExecutableDeploy order 5
     */
    @Override
    public byte getOrder() {
        return 0x5;
    }

    /**
     * Implements the Transfer encoder
     */
    @Override
    public void encode(CLValueEncoder clve)
            throws IOException, CLValueEncodeException, DynamicInstanceException, NoSuchTypeException {
        String a = Hex.toHexString(clve.toByteArray());
        clve.write(getOrder());
        String b = Hex.toHexString(clve.toByteArray());
        clve.writeInt(args.size());
        String c = Hex.toHexString(clve.toByteArray());
        for (NamedArg<?> namedArg : args) {
            namedArg.encode(clve);
        }
        String d = Hex.toHexString(clve.toByteArray());
        String e = Hex.toHexString(clve.toByteArray());
    }
}