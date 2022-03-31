package com.syntifi.casper.sdk.service;

import com.googlecode.jsonrpc4j.ExceptionResolver;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.googlecode.jsonrpc4j.JsonRpcMethod;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcParamsPassMode;
import com.googlecode.jsonrpc4j.ProxyUtil;
import com.syntifi.casper.sdk.exception.CasperClientExceptionResolver;
import com.syntifi.casper.sdk.identifier.block.BlockIdentifier;
import com.syntifi.casper.sdk.identifier.dictionary.DictionaryIdentifier;
import com.syntifi.casper.sdk.model.account.AccountData;
import com.syntifi.casper.sdk.model.auction.AuctionData;
import com.syntifi.casper.sdk.model.balance.BalanceData;
import com.syntifi.casper.sdk.model.block.JsonBlockData;
import com.syntifi.casper.sdk.model.deploy.Deploy;
import com.syntifi.casper.sdk.model.deploy.DeployData;
import com.syntifi.casper.sdk.model.deploy.DeployResult;
import com.syntifi.casper.sdk.model.dictionary.DictionaryData;
import com.syntifi.casper.sdk.model.era.EraInfoData;
import com.syntifi.casper.sdk.model.peer.PeerData;
import com.syntifi.casper.sdk.model.stateroothash.StateRootHashData;
import com.syntifi.casper.sdk.model.status.StatusData;
import com.syntifi.casper.sdk.model.storedvalue.StoredValueData;
import com.syntifi.casper.sdk.model.transfer.TransferData;

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
     * Get network peers data
     *
     * @return Object holding the api version and peer list
     */
    @JsonRpcMethod("info_get_peers")
    PeerData getPeerData();

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
     * Returns a stored value from the network
     *
     * @param stateRootHash Hash of the state root
     * @param key           `casper_types::Key` as formatted string
     * @param path          The path components starting from the key as base
     * @return Object holding the api version, the merkle proof and the stored value
     * data
     */
    @JsonRpcMethod("state_get_item")
    StoredValueData getStateItem(@JsonRpcParam("state_root_hash") String stateRootHash,
                                 @JsonRpcParam("key") String key, @JsonRpcParam("path") List<String> path);

    /**
     * Returns an EraInfo from the network
     *
     * @param blockIdentifier BlockIdentifier data
     * @return Object holding api version and EraInfo
     */
    @JsonRpcMethod("chain_get_era_info_by_switch_block")
    EraInfoData getEraInfoBySwitchBlock(@JsonRpcParam("block_identifier") BlockIdentifier blockIdentifier);

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
     * Returns the current status of the node
     *
     * @return Object holding the apiversion, minimal block information, build
     * version and other properties
     */
    @JsonRpcMethod("info_get_status")
    StatusData getStatus();

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
     * Returns the Auction info for a given block
     *
     * @param blockIdentifier BlockIdentifier data
     * @return Object holding the api version and auction state data
     */
    @JsonRpcMethod("state_get_auction_info")
    AuctionData getStateAuctionInfo(@JsonRpcParam("block_identifier") BlockIdentifier blockIdentifier);

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
     * Fetches balance value
     *
     * @param stateRootHash the hash of state root
     * @param purseUref     formatted URef
     * @return Result for "state_get_balance" RPC response
     */
    @JsonRpcMethod("state_get_balance")
    BalanceData getBalance(@JsonRpcParam("state_root_hash") String stateRootHash,
                           @JsonRpcParam("purse_uref") String purseUref);

    /**
     * Sends a deploy to be received by the network
     *
     * @param deploy the deploy object to send to the network
     * @return Object holding the api version and the deploy hash
     */
    @JsonRpcMethod(value = "account_put_deploy", paramsPassMode = JsonRpcParamsPassMode.ARRAY)
    DeployResult putDeploy(Deploy deploy);

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
        JsonRpcHttpClient client = new JsonRpcHttpClient(objectMapper, new URL("http", ip, port, "/rpc"),
                newHeaders);

        ExceptionResolver exceptionResolver = new CasperClientExceptionResolver();
        client.setExceptionResolver(exceptionResolver);

        return ProxyUtil.createClientProxy(CasperService.class.getClassLoader(), CasperService.class, client);
    }
}
