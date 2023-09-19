package com.casper.sdk.service;

import com.casper.sdk.exception.CasperClientExceptionResolver;
import com.casper.sdk.identifier.block.BlockIdentifier;
import com.casper.sdk.identifier.dictionary.DictionaryIdentifier;
import com.casper.sdk.identifier.global.GlobalStateIdentifier;
import com.casper.sdk.identifier.purse.PurseIdentifier;
import com.casper.sdk.model.account.AccountData;
import com.casper.sdk.model.auction.AuctionData;
import com.casper.sdk.model.balance.GetBalanceData;
import com.casper.sdk.model.balance.QueryBalanceData;
import com.casper.sdk.model.block.JsonBlockData;
import com.casper.sdk.model.deploy.Deploy;
import com.casper.sdk.model.deploy.DeployData;
import com.casper.sdk.model.deploy.DeployResult;
import com.casper.sdk.model.deploy.SpeculativeDeployData;
import com.casper.sdk.model.dictionary.DictionaryData;
import com.casper.sdk.model.era.EraInfoData;
import com.casper.sdk.model.globalstate.GlobalStateData;
import com.casper.sdk.model.peer.PeerData;
import com.casper.sdk.model.stateroothash.StateRootHashData;
import com.casper.sdk.model.status.ChainspecData;
import com.casper.sdk.model.status.StatusData;
import com.casper.sdk.model.storedvalue.StoredValueData;
import com.casper.sdk.model.transfer.TransferData;
import com.casper.sdk.model.uref.URef;
import com.casper.sdk.model.validator.ValidatorChangeData;
import com.googlecode.jsonrpc4j.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Interface to be used as Dynamic Proxy for RPC method operation
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public interface CasperService {

    /**
     * Builds a CasperService for the node ip/port pair
     *
     * @param ip   the peer ip to connect to
     * @param port the service port of the peer
     * @return A Dynamic Proxy to CasperService
     * @throws MalformedURLException is thrown if ip/port are not compliant
     */
    static CasperService usingPeer(String ip, int port) throws MalformedURLException {
        CasperObjectMapper objectMapper = new CasperObjectMapper();
        Map<String, String> newHeaders = new HashMap<>();
        newHeaders.put("Content-Type", "application/json");
        JsonRpcHttpClient client;
        if(port == 443)  client = new JsonRpcHttpClient(objectMapper, new URL("https", ip, port, "/rpc"),
                    newHeaders);
        else client = new JsonRpcHttpClient(objectMapper, new URL("http", ip, port, "/rpc"),
                    newHeaders);

        ExceptionResolver exceptionResolver = new CasperClientExceptionResolver();
        client.setExceptionResolver(exceptionResolver);

        return ProxyUtil.createClientProxy(CasperService.class.getClassLoader(), CasperService.class, client);
    }

    static CasperService usingPeer(String ip, int port,HashMap<String,String> extraHeaders) throws MalformedURLException {
        CasperObjectMapper objectMapper = new CasperObjectMapper();
        Map<String, String> newHeaders = new HashMap<>();
        newHeaders.put("Content-Type", "application/json");
        newHeaders.putAll(extraHeaders);
        JsonRpcHttpClient client;
        if(port == 443)  client = new JsonRpcHttpClient(objectMapper, new URL("https", ip, port, "/rpc"),
                newHeaders);
        else client = new JsonRpcHttpClient(objectMapper, new URL("http", ip, port, "/rpc"),
                newHeaders);
        ExceptionResolver exceptionResolver = new CasperClientExceptionResolver();
        client.setExceptionResolver(exceptionResolver);

        return ProxyUtil.createClientProxy(CasperService.class.getClassLoader(), CasperService.class, client);
    }

    //region INFORMATIONAL METHODS

    /**
     * Get the latest block info
     *
     * @return Object holding the api version and block
     */
    @JsonRpcMethod("chain_get_block")
    JsonBlockData getBlock();

    /**
     * Retrieve block info by its {@link BlockIdentifier}
     *
     * @param blockIdentifier BlockIdentifier data
     * @return Object holding the api version and block
     */
    @JsonRpcMethod("chain_get_block")
    JsonBlockData getBlock(@JsonRpcParam("block_identifier") BlockIdentifier blockIdentifier);

    /**
     * Retrieve last block's transfers
     *
     * @return Object holding the api version and transfer data
     */
    @JsonRpcMethod("chain_get_block_transfers")
    TransferData getBlockTransfers();

    /**
     * Retrieve block transfers by its {@link BlockIdentifier}
     *
     * @param blockIdentifier BlockIdentifier data
     * @return Object holding the api version and transfer data
     */
    @JsonRpcMethod("chain_get_block_transfers")
    TransferData getBlockTransfers(@JsonRpcParam("block_identifier") BlockIdentifier blockIdentifier);

    /**
     * Returns a state root hash at the last Block
     *
     * @return Object holding the api version and state root hash data
     */
    @JsonRpcMethod("chain_get_state_root_hash")
    StateRootHashData getStateRootHash();

    /**
     * Returns a state root hash at a given a {@link BlockIdentifier}
     *
     * @param blockIdentifier BlockIdentifier data
     * @return Object holding the api version and state root hash data
     */
    @JsonRpcMethod("chain_get_state_root_hash")
    StateRootHashData getStateRootHash(@JsonRpcParam("block_identifier") BlockIdentifier blockIdentifier);

    /**
     * Returns a Deploy from the network
     *
     * @param deployHash The deploy hash
     * @return Object holding the api version, the deploy and the map of block hash
     * to execution result
     */
    @JsonRpcMethod("info_get_deploy")
    DeployData getDeploy(@JsonRpcParam("deploy_hash") String deployHash);

    /**
     * Returns the global state data given a {@link GlobalStateIdentifier}, key and paths
     *
     * @param stateIdentifier GlobalStateIndentifier: block hash or state root hash
     * @param key             casper_types::Key as a formatted string.
     * @param path            The path components starting from the key as base.
     * @return Object holding the api version, block header, merkle proof and stored values
     */
    @JsonRpcMethod("query_global_state")
    GlobalStateData queryGlobalState(@JsonRpcParam("state_identifier") GlobalStateIdentifier stateIdentifier,
                                     @JsonRpcParam("key") String key,
                                     @JsonRpcParam("path") String[] path);

    /**
     * Returns an Account from the network
     *
     * @param publicKey       the account's public key
     * @param blockIdentifier BlockIdentifier data
     * @return Object holding the api version, the account data and the merkle proof
     */
    @JsonRpcMethod("state_get_account_info")
    AccountData getStateAccountInfo(@JsonRpcParam("public_key") String publicKey,
                                    @JsonRpcParam("block_identifier") BlockIdentifier blockIdentifier);

    /**
     * Fetches balance value
     *
     * @param stateRootHash the hash of state root
     * @param purseUref     formatted URef
     * @return Result for "state_get_balance" RPC response
     */
    @JsonRpcMethod("state_get_balance")
    GetBalanceData getBalance(@JsonRpcParam("state_root_hash") String stateRootHash,
                              @JsonRpcParam("purse_uref") URef purseUref);

    /**
     * Lookup a dictionary item via a Contract's named keys. Returns an item from a
     * Dictionary given the AccountNamedKey/ContractNamedKey/Dictionary/Uref
     *
     * @param stateRootHash        the hash of state root
     * @param dictionaryIdentifier any concrete DictionaryIdentifier
     * @return Object holding the api version, the dictionary key, the merkle proof
     * and the stored value
     */
    @JsonRpcMethod("state_get_dictionary_item")
    DictionaryData getStateDictionaryItem(@JsonRpcParam("state_root_hash") String stateRootHash,
                                          @JsonRpcParam("dictionary_identifier") DictionaryIdentifier dictionaryIdentifier);

    /**
     * Get network peers data
     *
     * @return Object holding the api version and peer list
     */
    @JsonRpcMethod("info_get_peers")
    PeerData getPeerData();

    /**
     * Returns the current status of the node
     *
     * @return Object holding the apiversion, minimal block information, build
     * version and other properties
     */
    @JsonRpcMethod("info_get_status")
    StatusData getStatus();
    //endregion

    //region TRANSACTIONAL METHODS

    /**
     * Sends a deploy to be received by the network
     *
     * @param deploy the deploy object to send to the network
     * @return Object holding the api version and the deploy hash
     */
    @JsonRpcMethod(value = "account_put_deploy", paramsPassMode = JsonRpcParamsPassMode.ARRAY)
    DeployResult putDeploy(Deploy deploy);

    /**
     * The speculative_exec endpoint provides a method to execute a Deploy
     * without committing it to global state
     *
     * @param blockIdentifier BlockIdentifier data
     * @param deploy          the deploy object to send to the network
     * @return Object holding the api version and the deploy hash
     */
    @JsonRpcMethod("speculative_exec")
    SpeculativeDeployData speculativeExec(@JsonRpcParam("block_identifier") BlockIdentifier blockIdentifier,
                                 @JsonRpcParam("deploy") Deploy deploy);

    /**
     * The speculative_exec endpoint provides a method to execute a Deploy
     * without committing it to global state
     *
     * @param deploy          the deploy object to send to the network
     * @return Object holding the api version and the deploy hash
     */
    @JsonRpcMethod("speculative_exec")
    SpeculativeDeployData speculativeExec(@JsonRpcParam("deploy") Deploy deploy);
    //endregion

    //region PROOF OF STAKE METHODS

    /**
     * Returns the Auction info for a given block
     *
     * @param blockIdentifier BlockIdentifier data
     * @return Object holding the api version and auction state data
     */
    @JsonRpcMethod("state_get_auction_info")
    AuctionData getStateAuctionInfo(@JsonRpcParam("block_identifier") BlockIdentifier blockIdentifier);

    /**
     * Returns the Validator Changes in between two eras
     *
     * @return Object holding the api version and the validator change data
     */
    @JsonRpcMethod("info_get_validator_changes")
    ValidatorChangeData getValidatorsChanges();

    /**
     * Returns an EraInfo from the network
     *
     * @param blockIdentifier BlockIdentifier data
     * @return Object holding api version and EraInfo
     */
    @JsonRpcMethod("chain_get_era_info_by_switch_block")
    EraInfoData getEraInfoBySwitchBlock(@JsonRpcParam("block_identifier") BlockIdentifier blockIdentifier);

    /**
     * Returns an EraInfo for a given block
     *
     * @param blockIdentifier BlockIdentifier data
     * @return Object holding api version and EraInfo
     */
    @JsonRpcMethod("chain_get_era_summary")
    EraInfoData getEraSummary(@JsonRpcParam("block_identifier") BlockIdentifier blockIdentifier);
    //endregion

    //region TO BE IMPLEMENTED ON 1.5.0

    /**
     * Fetches balance value
     *
     * @param stateIdentifier GlobalStateIndentifier: block hash or state root hash
     * @param purseIdentifier The identifier to obtain the purse corresponding to balance query.
     * @return Result for "query_balance" RPC response
     */
    @JsonRpcMethod("query_balance")
    QueryBalanceData queryBalance(@JsonRpcParam("state_identifier") GlobalStateIdentifier stateIdentifier,
                                  @JsonRpcParam("purse_identifier") PurseIdentifier purseIdentifier);

    /**
     * Fetches chainspec file as bytes
     *
     * @return the ChainspecData holder object
     */
    @JsonRpcMethod("info_get_chainspec")
    ChainspecData getChainspec();
    //endregion

    //region DEPRECATED METHODS
    /**
     * Returns a stored value from the network. This RPC is deprecated, use `query_global_state` instead"
     *
     * @param stateRootHash Hash of the state root
     * @param key           `casper_types::Key` as formatted string
     * @param path          The path components starting from the key as base
     * @return Object holding the api version, the merkle proof and the stored value
     * data
     */
    @Deprecated()
    @JsonRpcMethod("state_get_item")
    StoredValueData getStateItem(@JsonRpcParam("state_root_hash") String stateRootHash,
                                 @JsonRpcParam("key") String key, @JsonRpcParam("path") List<String> path);
    //endregion
}
