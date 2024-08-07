package com.casper.sdk.model.key;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The entry point address.
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@Getter
public enum EntryPointAddr implements Tag {

    @JsonProperty("VmCasperV1")
    VM_CASPER_V1((byte) 0),
    @JsonProperty("VmCasperV1")
    VM_CASPER_V2((byte) 1);

    private final byte byteTag;

    public static EntryPointAddr getByTag(final byte byteTag) {
        for (EntryPointAddr addr : values()) {
            if (addr.byteTag == byteTag)
                return addr;
        }
        throw new IllegalArgumentException("No such EntryPointAddr: " + byteTag);
    }
}
