package com.syntifi.casper.sdk.model.clvalue;

import com.syntifi.casper.sdk.model.clvalue.cltype.AbstractCLType;

import lombok.EqualsAndHashCode;

/**
 * Abstract class for those CLValues which have a child collection
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractCLValueWithChildren<T, P extends AbstractCLType> extends AbstractCLValue<T, P> {
    protected abstract void setChildTypes();
}
