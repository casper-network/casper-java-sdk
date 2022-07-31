package com.casper.sdk.model.deploy.executabledeploy;

import com.casper.sdk.exception.CLValueEncodeException;
import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.deploy.NamedArg;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.casper.sdk.exception.DynamicInstanceException;
import com.casper.sdk.model.clvalue.encdec.CLValueEncoder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.util.List;

/**
 * Abstract Executable Deploy Item containing the StoredContractByHash.
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see ExecutableDeployItem
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeName("StoredContractByHash")
public class StoredContractByHash implements ExecutableDeployItem {

    /**
     * Hex-encoded Hash
     */
    private String hash;

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
     * {@link ExecutableDeployItem} order 1
     */
    @Override
    public byte getOrder() {
        return 0x1;
    }

    /**
     * Implements the StoredContractByHAsh encoder
     */
    @Override
    public void encode(CLValueEncoder clve, boolean encodeType)
            throws IOException, CLValueEncodeException, DynamicInstanceException, NoSuchTypeException {
        clve.write(getOrder());
        clve.writeString(getHash());
        clve.writeString(getEntryPoint());
        clve.writeInt(args.size());
        for (NamedArg<?> namedArg : args) {
            namedArg.encode(clve, true);
        }
    }

}