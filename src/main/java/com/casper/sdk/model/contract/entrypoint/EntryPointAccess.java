package com.casper.sdk.model.contract.entrypoint;

import com.casper.sdk.jackson.deserializer.EntryPointAccessDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Interface  describing the possible access control options for a contract entry point (method).
 *
 * @author ian@meywood.com
 */
@JsonDeserialize(using = EntryPointAccessDeserializer.class)
public interface EntryPointAccess {

}
