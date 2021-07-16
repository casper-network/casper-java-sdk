package com.casper.sdk.service.http.rpc;

import static com.casper.sdk.Properties.*;

import com.casper.sdk.domain.Deploy;
import com.casper.sdk.domain.DeployService;
import com.casper.sdk.json.JsonConversionService;
import com.casper.sdk.service.HashService;
import com.casper.sdk.service.MethodEnums;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
        return (result.isEmpty()) ? null : MethodEnums.STATE_ROOT_HASH.getValue(result.get());
    }

    public String getAccountInfo(final String accountKey) throws Throwable {

        final Optional<String> result = httpMethods.rpcCallMethod(new Method(STATE_GET_ITEM,
                        Map.of(
                                "state_root_hash", getStateRootHash(),
                                "key", "account-hash-" + hashService.getAccountHash(accountKey),
                                "path", Collections.emptyList()
                        )
                )
        );

        return (result.isEmpty()) ? null : result.get();
    }

    public String getAccountBalance(final String accountKey) throws Throwable {

        final Optional<String> result = httpMethods.rpcCallMethod(
                new Method(STATE_GET_BALANCE,
                        Map.of(
                                "state_root_hash", getStateRootHash(),
                                "purse_uref", getAccountMainPurseURef(accountKey)
                        )
                )
        );

        return (result.isEmpty()) ? null : MethodEnums.STATE_GET_BALANCE.getValue(result.get());
    }

    public String getAccountMainPurseURef(final String accountKey) throws Throwable {

        final Optional<String> result = httpMethods.rpcCallMethod(new Method(STATE_GET_ITEM,
                        Map.of(
                                "key", "account-hash-" + hashService.getAccountHash(accountKey),
                                "state_root_hash", getStateRootHash(),
                                "path", Collections.emptyList()
                        )
                )
        );

        return (result.isEmpty()) ? null : MethodEnums.STATE_GET_ITEM.getValue(result.get());
    }

    public String getAuctionInfo() throws Throwable {

        final Optional<String> result = httpMethods.rpcCallMethod(new Method(STATE_GET_AUCTION_INFO, new HashMap<>()));

        return (result.isEmpty()) ? null : MethodEnums.STATE_GET_AUCTION_INFO.getValue(result.get());
    }

    public String getNodePeers() throws Throwable {

        final Optional<String> result = httpMethods.rpcCallMethod(new Method(INFO_GET_PEERS, new HashMap<>()));

        return (result.isEmpty()) ? null : MethodEnums.INFO_GET_PEERS.getValue(result.get());
    }

    public String getNodeStatus() throws Throwable {

        final Optional<String> result = httpMethods.rpcCallMethod(new Method(INFO_GET_STATUS, new HashMap<>()));

        return (result.isEmpty()) ? null : MethodEnums.INFO_GET_STATUS.getValue(result.get());
    }

    public String putDeploy(final Deploy signedDeploy) throws Throwable {

        final int size = deployService.deploySizeInBytes(signedDeploy);

        if (size > ONE_MEGABYTE) {
            throw new IllegalArgumentException(String.format(DEPLOY_TOO_LARGE_MSG, size));
        }

        final Optional<String> result = httpMethods.rpcCallMethod(
                new Method(ACCOUNT_PUT_DEPLOY, Map.of(DEPLOY, signedDeploy))
        );

        return (result.isEmpty()) ? null : MethodEnums.ACCOUNT_PUT_DEPLOY.getValue(result.get());
    }
}
