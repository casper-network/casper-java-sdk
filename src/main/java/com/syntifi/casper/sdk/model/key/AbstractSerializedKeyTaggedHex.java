package com.syntifi.casper.sdk.model.key;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.syntifi.casper.sdk.model.clvalue.encdec.StringByteHelper;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Hex-encoded key, including the tag byte.
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"tag", "key"})
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
