package com.casper.sdk.service;

import com.casper.sdk.domain.Deploy;
import com.casper.sdk.domain.DeployUtil;
import com.casper.sdk.json.JsonConversionService;
import com.casper.sdk.service.http.rpc.HttpMethods;
import com.casper.sdk.service.http.rpc.Method;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.casper.sdk.Properties.*;

/**
 * Service to query the chain Methods call the HTTP methods with an instantiated method object
 */
public class QueryService {

    public static final String DEPLOY = "deploy";
    private static final int ONE_MEGABYTE = 1048576;
    private final HttpMethods httpMethods;
    private final HashService hashService;
    private final JsonConversionService jsonConversionService;

    public QueryService() {
        this.httpMethods = new HttpMethods();
        this.hashService = HashService.getInstance();
        this.jsonConversionService = new JsonConversionService();
    }

    public String getStateRootHash() throws Throwable {

        final Optional<String> result = httpMethods.rpcCallMethod(new Method(CHAIN_GET_STATE_ROOT_HASH));
        return (result.isEmpty()) ? null: MethodEnums.STATE_ROOT_HASH.getValue(result.get());

    }

    public String getAccountInfo(final String accountKey) throws Throwable {

        final Optional<String> result = httpMethods.rpcCallMethod(new Method(STATE_GET_ITEM,
                new HashMap<>() {
                    {
                        put("state_root_hash", getStateRootHash());
                        put("key", "account-hash-" + hashService.getAccountHash(accountKey));
                        put("path", Collections.emptyList());
                    }
                }));

        return (result.isEmpty()) ? null : result.get();

    }

    public String getAccountBalance(final String accountKey) throws Throwable {

        final Optional<String> result = httpMethods.rpcCallMethod(new Method(STATE_GET_BALANCE,
                new HashMap<>() {
                    {
                        put("state_root_hash", getStateRootHash());
                        put("purse_uref", getAccountMainPurseURef(accountKey));
                    }
                }));

        return (result.isEmpty()) ? null : MethodEnums.STATE_GET_BALANCE.getValue(result.get());

    }

    public String getAccountMainPurseURef(final String accountKey) throws Throwable {

        final Optional<String> result = httpMethods.rpcCallMethod(new Method(STATE_GET_ITEM,
                new HashMap<>() {
                    {
                        put("state_root_hash", getStateRootHash());
                        put("key", "account-hash-" + hashService.getAccountHash(accountKey));
                        put("path", Collections.emptyList());
                    }
                }));

        return (result.isEmpty()) ? null
                : MethodEnums.STATE_GET_ITEM.getValue(result.get());

    }

    public String getAuctionInfo() throws Throwable {

        final Optional<String> result = httpMethods.rpcCallMethod(new Method(STATE_GET_AUCTION_INFO,
                new HashMap<>()));

        return (result.isEmpty()) ? null : MethodEnums.STATE_GET_AUCTION_INFO.getValue(result.get());

    }

    public String getNodePeers() throws Throwable {

        final Optional<String> result = httpMethods.rpcCallMethod(new Method(INFO_GET_PEERS,
                new HashMap<>()));

        return (result.isEmpty()) ? null : MethodEnums.INFO_GET_PEERS.getValue(result.get());

    }

    public String getNodeStatus() throws Throwable {

        final Optional<String> result = httpMethods.rpcCallMethod(new Method(INFO_GET_STATUS,
                new HashMap<>()));

        return (result.isEmpty()) ? null : MethodEnums.INFO_GET_STATUS.getValue(result.get());

    }


    public String putDeploy(final Deploy signedDeploy) throws Throwable {

        final int size = DeployUtil.deploySizeInBytes(signedDeploy);
        if (size > ONE_MEGABYTE) {
            throw new IllegalArgumentException(
                    "Deploy can not be send, because it's too large: " + size + " bytes. Max size is 1 megabyte."
            );
        }

        final Map<String, Object> deploy = Map.of(DEPLOY, jsonConversionService.toJson(signedDeploy));
        final Optional<String> result = httpMethods.rpcCallMethod(
                new Method(ACCOUNT_PUT_DEPLOY, deploy)
        );

        return (result.isEmpty()) ? null : MethodEnums.ACCOUNT_PUT_DEPLOY.getValue(result.get());
    }
}
