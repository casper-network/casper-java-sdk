package com.casper.sdk.service.http.rpc;

import com.casper.sdk.service.HashService;
import com.casper.sdk.service.MethodEnums;
import com.casper.sdk.service.json.JsonConversionService;
import com.casper.sdk.service.serialization.util.CollectionUtils;
import com.casper.sdk.types.Deploy;
import com.casper.sdk.types.DeployService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

import static com.casper.sdk.Properties.*;

/**
 * Service to query the chain Methods call the HTTP methods with an instantiated method object
 */
public class NodeClient {

    public static final String DEPLOY = "deploy";
    public static final String DEPLOY_TOO_LARGE_MSG = "Deploy can not be send, because it's too large: %d bytes. Max size is 1 megabyte.";
    private static final int ONE_MEGABYTE = 1048576;
    private final HttpMethods httpMethods;
    private final HashService hashService;
    private final DeployService deployService;

    public NodeClient(final DeployService deployService,
                      final HashService hashService,
                      final JsonConversionService jsonConversionService) {
        this.deployService = deployService;
        this.hashService = hashService;
        this.httpMethods = new HttpMethods(jsonConversionService);
    }

    public String getStateRootHash() throws Throwable {

        final Optional<String> result = httpMethods.rpcCallMethod(new Method(CHAIN_GET_STATE_ROOT_HASH));
        return result.isPresent() ? MethodEnums.STATE_ROOT_HASH.getValue(result.get()) : null;
    }

    public String getAccountInfo(final String accountKey) throws Throwable {

        final Optional<String> result = httpMethods.rpcCallMethod(new Method(STATE_GET_ITEM,
                        CollectionUtils.Map.of(
                                "state_root_hash", getStateRootHash(),
                                "key", "account-hash-" + hashService.getAccountHash(accountKey),
                                "path", Collections.emptyList()
                        )
                )
        );

        return result.orElse(null);
    }

    public String getAccountBalance(final String accountKey) throws Throwable {

        final Optional<String> result = httpMethods.rpcCallMethod(
                new Method(STATE_GET_BALANCE,
                        CollectionUtils.Map.of(
                                "state_root_hash", getStateRootHash(),
                                "purse_uref", getAccountMainPurseURef(accountKey)
                        )
                )
        );

        return result.isPresent() ? MethodEnums.STATE_GET_BALANCE.getValue(result.get()) : null;
    }

    public String getAccountMainPurseURef(final String accountKey) throws Throwable {

        final Optional<String> result = httpMethods.rpcCallMethod(new Method(STATE_GET_ITEM,
                        CollectionUtils.Map.of(
                                "key", "account-hash-" + hashService.getAccountHash(accountKey),
                                "state_root_hash", getStateRootHash(),
                                "path", Collections.emptyList()
                        )
                )
        );

        return result.isPresent() ? MethodEnums.STATE_GET_ITEM.getValue(result.get()) : null;
    }

    public String getAuctionInfo() throws Throwable {

        final Optional<String> result = httpMethods.rpcCallMethod(new Method(STATE_GET_AUCTION_INFO, new HashMap<>()));

        return result.isPresent() ? MethodEnums.STATE_GET_AUCTION_INFO.getValue(result.get()) : null;
    }

    public String getNodePeers() throws Throwable {

        final Optional<String> result = httpMethods.rpcCallMethod(new Method(INFO_GET_PEERS, new HashMap<>()));

        return result.isPresent() ? MethodEnums.INFO_GET_PEERS.getValue(result.get()) : null;
    }

    public String getNodeStatus() throws Throwable {

        final Optional<String> result = httpMethods.rpcCallMethod(new Method(INFO_GET_STATUS, new HashMap<>()));

        return result.isPresent() ? MethodEnums.INFO_GET_STATUS.getValue(result.get()) : null;
    }

    public String putDeploy(final Deploy signedDeploy) throws Throwable {

        final int size = deployService.deploySizeInBytes(signedDeploy);

        if (size > ONE_MEGABYTE) {
            throw new IllegalArgumentException(String.format(DEPLOY_TOO_LARGE_MSG, size));
        }

        final Optional<String> result = httpMethods.rpcCallMethod(
                new Method(ACCOUNT_PUT_DEPLOY, CollectionUtils.Map.of(DEPLOY, signedDeploy))
        );

        return result.isPresent() ? MethodEnums.ACCOUNT_PUT_DEPLOY.getValue(result.get()) : null;
    }
}
