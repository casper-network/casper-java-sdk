package com.casper.sdk.model.clvalue.cltype;

import lombok.NoArgsConstructor;

/**
 * Base class for all types which are simple mappings
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@NoArgsConstructor
public abstract class AbstractCLTypeBasic extends AbstractCLType {
    protected AbstractCLTypeBasic(String typeName) {
        if (!this.getTypeName().equals(typeName)) {
            throw new IllegalArgumentException(
                    String.format("%s is an invalid type for %s", getClass().getSimpleName(), typeName));
        }
    }

    @Override
    public boolean isUndeserializable() {
        // Basic type can always be serialized
        return false;
    }
}
