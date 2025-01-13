package com.casper.sdk.model.contract.entrypoint;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Anyone can call this method (no access controls).
 *
 * @author ian@meywood.com
 */
@JsonTypeName("Public")
public class PublicAccess implements EntryPointAccess {
}
