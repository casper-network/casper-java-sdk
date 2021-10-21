package com.syntifi.casper.sdk.model.clvalue.cltype;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * CLType for {@link AbstractCLType.U8}
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLType
 * @since 0.0.1
 */
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, of = { "typeName" })
public class CLTypeU8 extends AbstractCLTypeBasic {
    private final String typeName = AbstractCLType.U8;

    @JsonCreator
    protected CLTypeU8(String typeName) {
        super(typeName);
    }
}
