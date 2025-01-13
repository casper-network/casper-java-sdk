package com.casper.sdk.model.contract.entrypoint;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Can't be accessed directly but are kept in the derived wasm bytes.
 *
 * @author ian@meywood.com
 */
@JsonTypeName("Template")
public class TemplateAccess implements EntryPointAccess {
}
