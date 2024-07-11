package com.casper.sdk.service;

import com.casper.sdk.exception.CasperClientExceptionResolver;
import com.casper.sdk.identifier.block.BlockIdentifier;
import com.casper.sdk.identifier.dictionary.DictionaryIdentifier;
import com.casper.sdk.identifier.entity.EntityIdentifier;
import com.casper.sdk.identifier.era.EraIdentifier;
import com.casper.sdk.identifier.global.GlobalStateIdentifier;
import com.casper.sdk.identifier.purse.PurseIdentifier;
import com.casper.sdk.model.account.AccountData;
import com.casper.sdk.model.auction.AuctionData;
import com.casper.sdk.model.balance.GetBalanceData;
import com.casper.sdk.model.balance.QueryBalanceData;
import com.casper.sdk.model.balance.QueryBalanceDetailsResult;
import com.casper.sdk.model.block.ChainGetBlockResult;
import com.casper.sdk.model.deploy.Deploy;
import com.casper.sdk.model.deploy.DeployData;
import com.casper.sdk.model.deploy.DeployResult;
import com.casper.sdk.model.deploy.SpeculativeDeployData;
import com.casper.sdk.model.dictionary.DictionaryData;
import com.casper.sdk.model.entity.StateEntityResult;
import com.casper.sdk.model.era.EraInfoData;
import com.casper.sdk.model.globalstate.GlobalStateData;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.peer.PeerData;
import com.casper.sdk.model.reward.GetRewardResult;
import com.casper.sdk.model.stateroothash.StateRootHashData;
import com.casper.sdk.model.status.ChainspecData;
import com.casper.sdk.model.status.StatusData;
import com.casper.sdk.model.storedvalue.StoredValueData;
import com.casper.sdk.model.transaction.GetTransactionResult;
import com.casper.sdk.model.transaction.PutTransactionResult;
import com.casper.sdk.model.transaction.Transaction;
import com.casper.sdk.model.transaction.TransactionHash;
import com.casper.sdk.model.transfer.TransferData;
import com.casper.sdk.model.uref.URef;
import com.casper.sdk.model.validator.ValidatorChangeData;
import com.googlecode.jsonrpc4j.*;
import org.apache.cxf.common.util.StringUtils;

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
     * Builds a CasperService for the node url.
     *
     * @param url               the peer url to connect to
     * @param additionalHeaders additional headers to be added to the request
     * @return A Dynamic Proxy to CasperService
     * @throws MalformedURLException is thrown if ip/port are not compliant
     */
    static CasperService usingPeer(URL url, final Map<String, String> additionalHeaders) throws MalformedURLException {
        if (StringUtils.isEmpty(url.getPath())) {
            url = new URL(url.getProtocol(), url.getHost(), url.getPort(), "/rpc");
        }

        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        if (additionalHeaders != null) {
            headers.putAll(additionalHeaders);
        }

        final JsonRpcHttpClient client = new JsonRpcHttpClient(new CasperObjectMapper(), url, headers);
        client.setExceptionResolver(new CasperClientExceptionResolver());

        return ProxyUtil.createClientProxy(CasperService.class.getClassLoader(), CasperService.class, client);
    }

    /**
     * Builds a CasperService for the node ip/port pair
     *
     * @param ip   the peer ip to connect to
     * @param port the service port of the peer
     * @return A Dynamic Proxy to CasperService
     * @throws MalformedURLException is thrown if ip/port are not compliant
     */
    static CasperService usingPeer(String ip, int port) throws MalformedURLException {
        return usingPeer(new URL("http", ip, port, "/rpc"), null);
    }

    //region INFORMATIONAL METHODS

    /**
     * Get the latest block info
     *
     * @return Object holding the api version and block
     */
    @JsonRpcMethod("chain_get_block")
    ChainGetBlockResult getBlock();

    /**
     * Retrieve block info by its {@link BlockIdentifier}
     *
     * @param blockIdentifier BlockIdentifier data
     * @return Object holding the api version and block
     */
    @JsonRpcMethod("chain_get_block")
    ChainGetBlockResult getBlock(@JsonRpcParam("block_identifier") BlockIdentifier blockIdentifier);

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
    DeployData getDeploy(@JsonRpcParam("deploy_hash") final String deployHash);

    /**
     * Returns a Transaction from the network
     *
     * @param transactionHash the hash of the transaction to obtain
     * @return Object holding the api version, the transaction and execution info
     */
    @JsonRpcMethod("info_get_transaction")
    GetTransactionResult getTransaction(@JsonRpcParam("transaction_hash") final TransactionHash transactionHash);

    /**
     * Returns the global state data given a {@link GlobalStateIdentifier}, key and paths
     *
     * @param stateIdentifier GlobalStateIdentifier: block hash or state root hash
     * @param key             casper_types::Key as a formatted string.
     * @param path            The path components starting from the key as base.
     * @return Object holding the api version, block header, merkle proof and stored values
     */
    @JsonRpcMethod("query_global_state")
    GlobalStateData queryGlobalState(@JsonRpcParam("state_identifier") GlobalStateIdentifier stateIdentifier,
                                     @JsonRpcParam("key") String key,
                                     @JsonRpcParam("path") String[] path);

    /**
     * Query for full balance information using a purse identifier and a state identifier.
     *
     * @param purseIdentifier The identifier to obtain the purse corresponding to balance query
     * @param stateIdentifier The identifier for the state used for the query, if none is passed, the latest block will be used.
     * @return the result for "query_balance_details" RPC response.
     */
    @JsonRpcMethod("query_balance_details")
    QueryBalanceDetailsResult queryBalanceDetails(@JsonRpcParam("purse_identifier") PurseIdentifier purseIdentifier,
                                                  @JsonRpcParam("state_identifier") GlobalStateIdentifier stateIdentifier);

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
     * @return Object holding the api version, minimal block information, build
     * version and other properties
     */
    @JsonRpcMethod("info_get_status")
    StatusData getStatus();


    /**
     * Returns an AddressableEntity from the network
     *
     * @param entityIdentifier entity identifier
     * @return object holding the AddressableEntity or LegacyAccount
     */
    @JsonRpcMethod("state_get_entity")
    StateEntityResult getStateEntity(@JsonRpcParam("entity_identifier") EntityIdentifier entityIdentifier);

    /**
     * Returns an AddressableEntity from the network
     *
     * @param entityIdentifier entity identifier
     * @param blockIdentifier block identifier
     * @return object holding the AddressableEntity or LegacyAccount
     */
    @JsonRpcMethod("state_get_entity")
    StateEntityResult getStateEntity(@JsonRpcParam("entity_identifier") EntityIdentifier entityIdentifier,
                                     @JsonRpcParam("block_identifier") BlockIdentifier blockIdentifier);


    /**
     * Obtains validator or delegator rewards.
     *
     * @param eraIdentifier the optional era identifier. If omitted, the last finalized era is used.
     * @param validator     the required public key
     * @param delegator     the optional delegator public key.  If provided, the rewards for the delegator are returned.
     *                      If omitted, the rewards for the validator are returned.
     * @return the result for "info_get_reward" RPC request
     */
    @JsonRpcMethod("info_get_reward")
    GetRewardResult getReward(@JsonRpcParam("era_identifier") EraIdentifier eraIdentifier,
                              @JsonRpcParam("validator") PublicKey validator,
                              @JsonRpcParam("delegator") PublicKey delegator);

    //endregion

    //region TRANSACTIONAL METHODS

    /**
     * Sends a transaction to be received by the network
     *
     * @param transaction the deploy object to send to the network
     * @return Object holding the api version and the deploy hash
     */
    @JsonRpcMethod(value = "account_put_transaction", paramsPassMode = JsonRpcParamsPassMode.ARRAY)
    PutTransactionResult putTransaction(final Transaction transaction);


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
     * @param deploy the deploy object to send to the network
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
     * @param stateIdentifier GlobalStateIdentifier: block hash or state root hash
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
     * Returns a stored value from the network. This RPC is deprecated, use `query_global_state` instead.
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

    /**
     * Sends a deploy to be received by the network
     *
     * @param deploy the deploy object to send to the network
     * @return Object holding the api version and the deploy hash
     * @deprecated use {@link #putTransaction(Transaction)} instead
     */
    @Deprecated
    @JsonRpcMethod(value = "account_put_deploy", paramsPassMode = JsonRpcParamsPassMode.ARRAY)
    DeployResult putDeploy(Deploy deploy);

    //endregion
}
