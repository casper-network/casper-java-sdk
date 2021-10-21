package com.syntifi.casper.sdk.model.clvalue.cltype;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * CLType for {@link AbstractCLType.BYTE_ARRAY}
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLType
 * @since 0.0.1
 */
@Getter
@EqualsAndHashCode(callSuper = false, of = { "typeName", "length" })
public class CLTypeByteArray extends AbstractCLType {
    private final String typeName = AbstractCLType.BYTE_ARRAY;

    @Setter
    @JsonProperty(AbstractCLType.BYTE_ARRAY)
    private int length;
}
