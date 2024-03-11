package com.casper.sdk.model.clvalue.cltype;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * CLType for {@link AbstractCLType#U256}
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLType
 * @since 0.0.1
 */

@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, of = {"typeName"})
public class CLTypeU256 extends AbstractCLTypeBasic {
    private final String typeName = U256;

    @JsonCreator
    protected CLTypeU256(final String typeName) {
        super(typeName);
    }
}
