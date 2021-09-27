package com.syntifi.casper.sdk.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.googlecode.jsonrpc4j.JsonRpcMethod;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.ProxyUtil;
import com.syntifi.casper.sdk.identifier.block.BlockIdentifierByHash;
import com.syntifi.casper.sdk.identifier.block.BlockIdentifierByHeight;
import com.syntifi.casper.sdk.model.block.JsonBlockData;
import com.syntifi.casper.sdk.model.peer.PeerData;
import com.syntifi.casper.sdk.model.stateroothash.StateRootHashData;
import com.syntifi.casper.sdk.model.storedvalue.StoredValueData;
import com.syntifi.casper.sdk.model.transfer.TransferData;

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
    public PeerData getPeerData();

    /**
     * Get latest block info
     * 
     * @return Object holding the api version and block
     */
    @JsonRpcMethod("chain_get_block")
    public JsonBlockData getBlock();

    /**
     * Retrieve block info by its hash
     * 
     * @param hash Block's hash
     * @return Object holding the api version and block
     */
    @JsonRpcMethod("chain_get_block")
    public JsonBlockData getBlock(@JsonRpcParam("block_identifier") BlockIdentifierByHash hash);

    /**
     * Retrieve block info by its height
     * 
     * @param heightFilter Block's height
     * @return Object holding the api version and block
     */
    @JsonRpcMethod("chain_get_block")
    public JsonBlockData getBlock(@JsonRpcParam("block_identifier") BlockIdentifierByHeight height);

    /**
     * Retrieve last block's transfers 
     * 
     * @param heightFilter Block's height
     * @return Object holding the api version and block
     */
    @JsonRpcMethod("chain_get_block_transfers")
    public TransferData getBlockTransfers();

    /**
     * Retrieve block transfers by its height
     * 
     * @param heightFilter Block's height
     * @return Object holding the api version and block
     */
    @JsonRpcMethod("chain_get_block_transfers")
    public TransferData getBlockTransfers(@JsonRpcParam("block_identifier") BlockIdentifierByHeight height);

    /**
     * Retrieve block transfers by its hash
     * 
     * @param hash Block's hash
     * @return Object holding the api version and block
     */
    @JsonRpcMethod("chain_get_block_transfers")
    public TransferData getBlockTransfers(@JsonRpcParam("block_identifier") BlockIdentifierByHash hash);

    /**
     * Returns a state root hash at the last Block 
     * 
     * @param heightFilter Block's height
     * @return Object holding the api version and block
     */
    @JsonRpcMethod("chain_get_state_root_hash")
    public StateRootHashData getStateRootHash();

    /**
     * Returns a state root hash at a given Block height
     * 
     * @param heightFilter Block's height
     * @return Object holding the api version and block
     */
    @JsonRpcMethod("chain_get_state_root_hash")
    public StateRootHashData getStateRootHash(@JsonRpcParam("block_identifier") BlockIdentifierByHeight height);

    /**
     * Returns a state root hash at a given Block hash
     * 
     * @param hash Block's hash
     * @return Object holding the api version and block
     */
    @JsonRpcMethod("chain_get_state_root_hash")
    public StateRootHashData getStateRootHash(@JsonRpcParam("block_identifier") BlockIdentifierByHash hash);

    /**
     * Returns a stored value from the network
     * 
     * @param stateRootHash Hash of the state root
     * @param key `casper_types::Key` as formatted string
     * @param path The path components starting from the key as base
     * @return Object holding the api version, the merkle proof and the stored_value
     */
    @JsonRpcMethod("state_get_item")
    public StoredValueData getStateItem(@JsonRpcParam("state_root_hash") String stateRootHash,
            @JsonRpcParam("key") String key, @JsonRpcParam("path") List<String> path);

    /**
     * Returns a Deploy from the network
     * 
     * @param deployHash The deploy hash
     * @return Object holding the api version, the deploy and the map of block hash to execution result
     */
    //@JsonRpcMethod("info_get_deploy")
    //public DeployData getDeploy(@JsonRpcParam("deploy_hash") String deployHash);

    // @JsonRpcMethod("info_get_status")
    // public CasperStatus getStatus();

    // @JsonRpcMethod("state_get_auction_info")
    // public CasperStatus getValidatorsInfo();

    /**
     * Builds a CasperService for the node ip/port pair
     * 
     * @param ip   the peer ip to connect to
     * @param port the service port of the peer
     * @return A Dynamic Proxy to CasperService
     * @throws MalformedURLException is thrown if ip/port are not compliant
     */
    public static CasperService usingPeer(String ip, int port) throws MalformedURLException {
        JsonRpcHttpClient client = new JsonRpcHttpClient(new URL("http", ip, port, "/rpc"));

        Map<String, String> newHeaders = new HashMap<>();
        newHeaders.put("Content-Type", "application/json");
        client.setHeaders(newHeaders);

        return ProxyUtil.createClientProxy(CasperService.class.getClassLoader(), CasperService.class, client);
    }
}
