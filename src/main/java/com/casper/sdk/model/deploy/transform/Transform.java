package com.casper.sdk.model.deploy.transform;

import com.casper.sdk.jackson.resolver.TransformResolver;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;

/**
 * Abstract Transform containing the actual transformation performed while
 * executing a deployment. It can be any of the following types:
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AddInt32
 * @see AddUInt64
 * @see AddUInt128
 * @see AddUInt256
 * @see AddUInt512
 * @see AddKeys
 * @see Failure
 * @see WriteAccount
 * @see WriteBid
 * @see WriteCLValue
 * @see WriteContract
 * @see WriteDeployInfo
 * @see WriteEraInfo
 * @see WriteTransfer
 * @see WriteWithdraw
 * @since 0.0.1
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonTypeResolver(TransformResolver.class)
public interface Transform {
}
