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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Abstract Executable Deploy Item containing the ModuleBytes of the contract.
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
@JsonTypeName("ModuleBytes")
public class ModuleBytes implements ExecutableDeployItem {

    /**
     * Module bytes
     */
    @JsonProperty("module_bytes")
    private String bytes;

    /**
     * @see NamedArg
     */
    private List<NamedArg<?>> args;


    /**
     * {@link ExecutableDeployItem} order 0
     */
    @Override
    public byte getOrder() {
        return 0x0;
    }

    /**
     * Implements the ModuleBytes encoder
     */
    @Override
    public void encode(CLValueEncoder clve, boolean encodeType) throws IOException, CLValueEncodeException, DynamicInstanceException, NoSuchTypeException  {
        clve.write(getOrder());
        clve.writeString(getBytes());
        clve.writeInt(args.size());
        for (NamedArg<?> namedArg : args) {
            namedArg.encode(clve, encodeType);
        }
    }
}
