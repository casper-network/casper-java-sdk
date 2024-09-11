package com.casper.sdk.model.transaction.kind;

import com.casper.sdk.model.key.Tag;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * @author carl@stormeye.co.uk
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ByteCodeKind {
    @JsonProperty("kind")
    private ByteCodes kind;

    @JsonProperty("bytes")
    private String bytes;

    public enum ByteCodes implements Tag {
        Empty(0),
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


