package com.casper.sdk.model.key;

import com.casper.sdk.exception.CLValueEncodeException;
import com.casper.sdk.exception.DynamicInstanceException;
import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.encdec.CLValueEncoder;
import com.casper.sdk.model.clvalue.encdec.StringByteHelper;
import com.casper.sdk.model.clvalue.encdec.interfaces.EncodableValue;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;

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
public abstract class AbstractSerializedKeyTaggedHex<T extends Tag> implements EncodableValue {

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
        return StringByteHelper.convertBytesToHex(new byte[]{this.tag.getByteTag()})
                + StringByteHelper.convertBytesToHex(this.getKey());
    }

    /**
     * Implements TaggedHEx encoder
     */
    @Override
    public void encode(CLValueEncoder clve, boolean encodeType)
            throws IOException, CLValueEncodeException, DynamicInstanceException, NoSuchTypeException {
        clve.write(getTag().getByteTag());
        clve.write(getKey());
    }
}
