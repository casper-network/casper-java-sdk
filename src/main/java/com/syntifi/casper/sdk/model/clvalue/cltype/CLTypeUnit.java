package com.syntifi.casper.sdk.model.clvalue.cltype;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * CLType for {@link AbstractCLType.UNIT}
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLType
 * @since 0.0.1
 */

@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, of = { "typeName" })
public class CLTypeUnit extends AbstractCLTypeBasic {
    private final String typeName = AbstractCLType.UNIT;

    @JsonCreator
    protected CLTypeUnit(String typeName) {
        super(typeName);
    }
}
