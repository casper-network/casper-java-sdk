package com.syntifi.casper.sdk.model.deploy.transform;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import com.syntifi.casper.sdk.jackson.resolver.TransformResolver;

/**
 * Abstract Transform containing the actual transformation performed while
 * executing a deploy. It can be any of the following types:
 * 
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
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonTypeResolver(TransformResolver.class)
public interface Transform {
}
