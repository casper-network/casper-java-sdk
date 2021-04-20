package com.casper.sdk.service;

import static com.casper.sdk.Properties.*;

import com.casper.sdk.service.http.rpc.HttpMethods;
import com.casper.sdk.service.http.rpc.Method;

import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

/**
 * Service to query the chain
 * Methods call the HTTP methods with an instantiated method object
 */
public class QueryService {

    private final HttpMethods httpMethods;

    public QueryService() {
        httpMethods = new HttpMethods();
    }

    public String getStateRootHash() throws Throwable {

        final Optional<String> result = httpMethods.rpcCallMethod(new Method(CHAIN_GET_STATE_ROOT_HASH));
        return (result.isEmpty()) ? null
                : MethodEnums.STATE_ROOT_HASH.getValue(result.get());

    }

    public String getAccountInfo(final String accountKey) throws Throwable {

        final Optional<String> result = httpMethods.rpcCallMethod(new Method(STATE_GET_ITEM,
                new HashMap<>() {
                    {
                        put("state_root_hash", getStateRootHash());
                        put("key", "account-hash-" + HashService.getAccountHash(accountKey));
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

        return (result.isEmpty()) ? null
                : MethodEnums.STATE_GET_BALANCE.getValue(result.get());

    }

    public String getAccountMainPurseURef(final String accountKey) throws Throwable {

        final Optional<String> result = httpMethods.rpcCallMethod(new Method(STATE_GET_AUCTION_INFO,
                new HashMap<>() {            {
                        put("state_root_hash", getStateRootHash());
                        put("key", "account-hash-" + HashService.getAccountHash(accountKey));
                        put("path", Collections.emptyList());
                    }
                }));

        return (result.isEmpty()) ? null
                : MethodEnums.STATE_GET_ITEM.getValue(result.get());

    }

    public String getAuctionInfo() throws Throwable {

        final Optional<String> result = httpMethods.rpcCallMethod(new Method(STATE_GET_AUCTION_INFO,
                new HashMap<>()));

        return (result.isEmpty()) ? null
                : MethodEnums.STATE_GET_AUCTION_INFO.getValue(result.get());

    }

    public String getNodePeers() throws Throwable {

        final Optional<String> result = httpMethods.rpcCallMethod(new Method(INFO_GET_PEERS,
                new HashMap<>()));

        return (result.isEmpty()) ? null
                : MethodEnums.INFO_GET_PEERS.getValue(result.get());

    }

    public String getNodeStatus() throws Throwable {

        final Optional<String> result = httpMethods.rpcCallMethod(new Method(INFO_GET_STATUS,
                new HashMap<>()));

        return (result.isEmpty()) ? null
                : MethodEnums.INFO_GET_STATUS.getValue(result.get());

    }


}
