package com.syntifi.casper.sdk.model.deploy.executabledeploy;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
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
 * Abstract Executable Deploy Item containing the StoredContractByName.
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 * @see ExecutableDeployItem
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeName("StoredContractByName")
public class StoredContractByName implements ExecutableDeployItem {

    /**
     * Contract name
     */
    private String name;

    /**
     * Entry Point
     */
    @JsonProperty("entry_point")
    private String entryPoint;

    /**
     * @see NamedArg
     */
    private List<NamedArg<?>> args;

    /**
     * @link ExecutableDeploy order 3
     */
    @Override
    public byte getOrder() {
        return 0x2;
    }

    /**
     * Implements the StoredContractByHash encoder
     */
    @Override
    public void encode(CLValueEncoder clve)
            throws IOException, CLValueEncodeException, DynamicInstanceException, NoSuchTypeException {
        String a = Hex.toHexString(clve.toByteArray());
        clve.write(getOrder());
        String b = Hex.toHexString(clve.toByteArray());
        clve.writeString(getName());
        String c = Hex.toHexString(clve.toByteArray());
        clve.writeString(getEntryPoint());
        String d = Hex.toHexString(clve.toByteArray());
        clve.writeInt(args.size());
        for (NamedArg<?> namedArg : args) {
            namedArg.encode(clve);
        }
        String e = Hex.toHexString(clve.toByteArray());
        String f = Hex.toHexString(clve.toByteArray());
    }
}