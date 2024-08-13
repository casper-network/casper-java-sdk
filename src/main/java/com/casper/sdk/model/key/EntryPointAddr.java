package com.casper.sdk.model.key;

import com.casper.sdk.exception.NoSuchKeyTagException;
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

    public static EntryPointAddr getByKeyName(final String keyName) throws NoSuchKeyTagException {

        final String lowerCaseKeyName = keyName.toLowerCase();
        if (lowerCaseKeyName.contains("v2")) {
            return EntryPointAddr.VM_CASPER_V2;
        } else if (lowerCaseKeyName.contains("v1")) {
            return EntryPointAddr.VM_CASPER_V1;
        }
        throw new NoSuchKeyTagException("No such key name: " + keyName);
    }

    public static EntryPointAddr getByTag(final byte byteTag) {
        for (EntryPointAddr addr : values()) {
            if (addr.byteTag == byteTag)
                return addr;
        }
        throw new IllegalArgumentException("No such EntryPointAddr: " + byteTag);
    }
}
