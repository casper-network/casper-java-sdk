package com.casper.sdk.model.transaction.target;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * The identifier of a `TransactionTarget::Stored`
 *
 * @author ian@meywood.com
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ByHash.class, name = "ByHash"),
        @JsonSubTypes.Type(value = ByName.class, name = "ByName"),
        @JsonSubTypes.Type(value = ByPackageName.class, name = "ByPackageName"),
        @JsonSubTypes.Type(value = ByPackageHash.class, name = "ByPackageHash")
})
public interface TransactionInvocationTarget {
}
