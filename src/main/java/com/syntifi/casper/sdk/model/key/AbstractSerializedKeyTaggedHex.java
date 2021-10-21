package com.syntifi.casper.sdk.model.key;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.syntifi.casper.sdk.model.clvalue.encdec.StringByteHelper;

import lombok.Data;

/**
 * Hex-encoded key, including the tag byte.
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
public abstract class AbstractSerializedKeyTaggedHex<T extends Tag> {

    /**
     * @see Tag
     */
    @JsonIgnore
    private T tag;

    /**
     * Hex-encoded key
     */
    @JsonIgnore
    private byte[] key;

    @JsonValue
    public String getAlgoTaggedHex() {
        return StringByteHelper.convertBytesToHex(new byte[] { this.tag.getByteTag() })
                + StringByteHelper.convertBytesToHex(this.getKey());
    }
}
