package com.casper.sdk.service;

import com.casper.sdk.service.http.rpc.HttpMethods;
import com.casper.sdk.service.http.rpc.Method;

import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

public class QueryService {

    private final String CHAIN_GET_STATE_ROOT_HASH = "chain_get_state_root_hash";
    private final String STATE_GET_ITEM = "state_get_item";
    private final String STATE_GET_BALANCE = "state_get_balance";

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

        final Optional<String> result = httpMethods.rpcCallMethod(new Method(STATE_GET_ITEM,
                new HashMap<>() {
                    {
                        put("state_root_hash", getStateRootHash());
                        put("key", "account-hash-" + HashService.getAccountHash(accountKey));
                        put("path", Collections.emptyList());
                    }
                }));

        return (result.isEmpty()) ? null
                : MethodEnums.STATE_GET_ITEM.getValue(result.get());

    }



}
