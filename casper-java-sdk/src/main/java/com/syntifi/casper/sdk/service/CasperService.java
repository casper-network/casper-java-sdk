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
import com.syntifi.casper.sdk.filter.block.CasperBlockByHashFilter;
import com.syntifi.casper.sdk.filter.block.CasperBlockByHeightFilter;
import com.syntifi.casper.sdk.model.block.CasperBlockData;
import com.syntifi.casper.sdk.model.peer.CasperPeerData;
import com.syntifi.casper.sdk.model.stateroothash.CasperStateRootHashData;
import com.syntifi.casper.sdk.model.storedvalue.CasperStoredValueData;
import com.syntifi.casper.sdk.model.transfer.CasperTransferData;

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
    public CasperPeerData getPeerData();

    /**
     * Get latest block info
     * 
     * @return Object holding the api version and block
     */
    @JsonRpcMethod("chain_get_block")
    public CasperBlockData getBlock();

    /**
     * Retrieve block info by its hash
     * 
     * @param hash Block's hash
     * @return Object holding the api version and block
     */
    @JsonRpcMethod("chain_get_block")
    public CasperBlockData getBlock(@JsonRpcParam("block_identifier") CasperBlockByHashFilter hash);

    /**
     * Retrieve block info by its height
     * 
     * @param heightFilter Block's height
     * @return Object holding the api version and block
     */
    @JsonRpcMethod("chain_get_block")
    public CasperBlockData getBlock(@JsonRpcParam("block_identifier") CasperBlockByHeightFilter height);

    /**
     * Retrieve last block's transfers 
     * 
     * @param heightFilter Block's height
     * @return Object holding the api version and block
     */
    @JsonRpcMethod("chain_get_block_transfers")
    public CasperTransferData getBlockTransfers();

    /**
     * Retrieve block transfers by its height
     * 
     * @param heightFilter Block's height
     * @return Object holding the api version and block
     */
    @JsonRpcMethod("chain_get_block_transfers")
    public CasperTransferData getBlockTransfers(@JsonRpcParam("block_identifier") CasperBlockByHeightFilter height);

    /**
     * Retrieve block transfers by its hash
     * 
     * @param hash Block's hash
     * @return Object holding the api version and block
     */
    @JsonRpcMethod("chain_get_block_transfers")
    public CasperTransferData getBlockTransfers(@JsonRpcParam("block_identifier") CasperBlockByHashFilter hash);


    /**
     * Retrieve last block's transfers 
     * 
     * @param heightFilter Block's height
     * @return Object holding the api version and block
     */
    @JsonRpcMethod("chain_get_state_root_hash")
    public CasperStateRootHashData getStateRootHash();

    /**
     * Retrieve block transfers by its height
     * 
     * @param heightFilter Block's height
     * @return Object holding the api version and block
     */
    @JsonRpcMethod("chain_get_state_root_hash")
    public CasperStateRootHashData getStateRootHash(@JsonRpcParam("block_identifier") CasperBlockByHeightFilter height);

    /**
     * Retrieve block transfers by its hash
     * 
     * @param hash Block's hash
     * @return Object holding the api version and block
     */
    @JsonRpcMethod("chain_get_state_root_hash")
    public CasperStateRootHashData getStateRootHash(@JsonRpcParam("block_identifier") CasperBlockByHashFilter hash);




    /**
     * Retrieve block info by its height
     * 
     * @param storedValueFilter Block's storedValue params
     * @return Object holding the api version and block
     */
    @JsonRpcMethod("state_get_item")
    public CasperStoredValueData getStateItem(@JsonRpcParam("state_root_hash") String stateRootHash,
            @JsonRpcParam("key") String key, @JsonRpcParam("path") List<String> path);

    // @JsonRpcMethod("info_get_deploy")
    // public CasperDeploy getDeploy(@JsonRpcParam("deploy_hash") String
    // deployHash);

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
