package com.casper.sdk.model.event.transaction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.syntifi.crypto.key.encdec.Hex;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Message represented as raw bytes.
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonTypeName("Bytes")
public class BytesMessagePayload implements MessagePayload<byte[]> {

    @JsonValue
    private byte[] message;

    @JsonCreator
    public BytesMessagePayload(final String message) {
        this.message = Hex.decode(message);
    }
}
