package com.casper.sdk.service.http.rpc;

import com.casper.sdk.Constants;
import com.casper.sdk.exceptions.ValueNotFoundException;
import com.casper.sdk.service.hash.HashService;
import com.casper.sdk.service.json.JsonConversionService;
import com.casper.sdk.service.serialization.util.CollectionUtils;
import com.casper.sdk.types.Deploy;
import com.casper.sdk.types.DeployService;
import com.casper.sdk.types.Digest;
import com.casper.sdk.types.URef;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.casper.sdk.service.http.rpc.MethodEnums.*;

/**
 * Service to query the chain Methods call the HTTP methods with an instantiated method object
 */
public class NodeClient {

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

    public String getStateRootHash() {
        return rcpCallMethodMap(
                new Method(Constants.CHAIN_GET_STATE_ROOT_HASH),
                MethodEnums.STATE_ROOT_HASH::getValue
        );
    }

    public String getAccountInfo(final String accountKey) {

        return rcpCallMethodMap(
                new Method(Constants.STATE_GET_ITEM,
                        CollectionUtils.Map.of(
                                "state_root_hash", getStateRootHash(),
                                "key", "account-hash-" + hashService.getAccountHash(accountKey),
                                "path", Collections.emptyList()
                        )
                ),
                MethodEnums.ACCOUNT_INFO::getValue
        );
    }

    public BigInteger getAccountBalance(final String accountKey) {

        return rcpCallMethodMap(
                new Method(Constants.STATE_GET_BALANCE,
                        CollectionUtils.Map.of(
                                "state_root_hash", getStateRootHash(),
                                "purse_uref", getAccountMainPurseURef(accountKey)
                        )
                ),
                result -> new BigInteger(MethodEnums.STATE_GET_BALANCE.getValue(result))
        );
    }

    public URef getAccountMainPurseURef(final String accountKey) {

        return rcpCallMethodMap(
                new Method(Constants.STATE_GET_ITEM,
                        CollectionUtils.Map.of(
                                "key", "account-hash-" + hashService.getAccountHash(accountKey),
                                "state_root_hash", getStateRootHash(),
                                "path", Collections.emptyList()
                        )
                ),
                result -> new URef(MethodEnums.STATE_GET_ITEM.getValue(result))
        );
    }

    public String getAuctionInfo() {

        return rcpCallMethodMap(
                new Method(Constants.STATE_GET_AUCTION_INFO, new HashMap<>()),
                MethodEnums.STATE_GET_AUCTION_INFO::getValue
        );
    }

    public String getNodePeers() {

        return rcpCallMethodMap(
                new Method(Constants.INFO_GET_PEERS, new HashMap<>()),
                MethodEnums.INFO_GET_PEERS::getValue
        );
    }

    public String getNodeStatus() {
        return rcpCallMethodMap(
                new Method(Constants.INFO_GET_STATUS, new HashMap<>()),
                MethodEnums.INFO_GET_STATUS::getValue
        );
    }

    public String putDeploy(final Deploy signedDeploy) {

        final int size = deployService.deploySizeInBytes(signedDeploy);

        if (size > ONE_MEGABYTE) {
            throw new IllegalArgumentException(String.format(DEPLOY_TOO_LARGE_MSG, size));
        }

        return rcpCallMethodMap(
                new Method(Constants.ACCOUNT_PUT_DEPLOY, CollectionUtils.Map.of(Constants.DEPLOY, signedDeploy)),
                MethodEnums.ACCOUNT_PUT_DEPLOY::getValue
        );
    }

    public Deploy getDeploy(final Digest deployHash) {

        return rcpCallMethodMap(
                new Method(Constants.INFO_GET_DEPLOY, CollectionUtils.Map.of(Constants.DEPLOY_HASH, deployHash.toString())),
                result -> deployService.fromJson(MethodEnums.INFO_GET_DEPLOY.getValue(result))
        );

    }

    public String getLatestBlockInfo() {
        return getChainBlockInfo(new HashMap<>());
    }

    public String getBlockInfo(final Digest blockHash) {
        final Map<String, Object> params = CollectionUtils.Map.of(
                "block_identifier",
                CollectionUtils.Map.of("Hash", blockHash.toString()
                ));
        return getChainBlockInfo(params);
    }

    public String getBlockInfoByHeight(final Number height) {
        final Map<String, Object> params = CollectionUtils.Map.of(
                "block_identifier",
                CollectionUtils.Map.of("Height", height.toString())
        );
        return getChainBlockInfo(params);
    }

    /**
     * Obtains the block info using the provided parameters
     *
     * @param params the parameters to obtain the block info with
     * @return the chain block info
     */
    String getChainBlockInfo(final Map<String, Object> params) {
        return rcpCallMethodMap(
                new Method(Constants.CHAIN_GET_BLOCK, params),
                CHAIN_GET_BLOCK::getValue
        );
    }

    /**
     * Obtain the RPC Schema
     *
     * @return the
     */
    public String getRpcSchema() {
        return rcpCallMethodMap(new Method(Constants.RPC_DISCOVER, new HashMap<>()), RPC_DISCOVER::getValue);
    }

    /**
     * Obtains the nodes chain block transfers
     *
     * @return the JSON of the chain block transfers
     */
    public String getBlockTransfers() {
        return rcpCallMethodMap(
                new Method(Constants.CHAIN_GET_BLOCK_TRANSFERS, new HashMap<>()),
                CHAIN_GET_BLOCK_TRANSFERS::getValue
        );
    }

    /**
     * Obtains the chain era info by switch block result as JSON
     *
     * @return Obtains
     */
    public String getEraInfoBySwitchBlock() {
        return rcpCallMethodMap(
                new Method(Constants.CHAIN_GET_ERA_INFO_BY_SWITCH_BLOCK, new HashMap<>()),
                CHAIN_GET_ERA_INFO_BY_SWITCH_BLOCK::getValue
        );
    }

    /**
     * Calls the RCP method and applies the provided map function to transform the result
     *
     * @param method the method to call against a node
     * @param mapper the mapper to transform the resulting JSON String
     * @param <T>    the type of object generated by the mapper function
     * @return the generated function
     */
    private <T> T rcpCallMethodMap(final Method method, final Function<String, ? extends T> mapper) {
        return httpMethods.rpcCallMethod(method)
                .map(mapper)
                .orElseThrow(() -> new ValueNotFoundException("For " + method));
    }
}
