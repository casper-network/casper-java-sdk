package com.casper.sdk.model.entity.contract;

import com.casper.sdk.model.key.Tag;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * A container for contract's Wasm bytes
 *
 * @author carl@stormeye.co.uk
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ByteCode {

    /** The type of Byte code */
    @JsonProperty("kind")
    private ByteCodes kind;
    /** Byte code */
    @JsonProperty("bytes")
    private String bytes;

    public enum ByteCodes implements Tag {
        /** Empty byte code */
        Empty(0),
        /** Byte code to be executed with the version 1 Casper execution engine */
        V1CasperWasm(1);
        private final byte tag;
        ByteCodes(final int tag) {
            this.tag = (byte) tag;
        }
        @Override
        public byte getByteTag() {
            return tag;
        }

    }
}
