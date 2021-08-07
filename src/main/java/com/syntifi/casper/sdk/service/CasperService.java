package com.syntifi.casper.sdk.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.googlecode.jsonrpc4j.JsonRpcMethod;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.ProxyUtil;
import com.syntifi.casper.sdk.filter.block.CasperBlockByHashFilter;
import com.syntifi.casper.sdk.filter.block.CasperBlockByHeightFilter;
import com.syntifi.casper.sdk.model.block.CasperBlockData;
import com.syntifi.casper.sdk.model.node.CasperNodeData;

/**
 * This interface is used as a Dynamic Proxy, serving all RPC methods
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.1.0
 */
public interface CasperService {
    /**
     * Get network peers data
     * 
     * @return Object holding the api version and peer list
     */
    @JsonRpcMethod("info_get_peers")
    public CasperNodeData getPeerData();

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
    public CasperBlockData getBlock(@JsonRpcParam("block_identifier") CasperBlockByHeightFilter heightFilter);

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
     * @param ip   the node ip to connect to
     * @param port the service port of the node
     * @return A Dynamic Proxy to CasperService
     * @throws MalformedURLException
     */
    public static CasperService withConfig(String ip, int port) throws MalformedURLException {
        var client = new JsonRpcHttpClient(new URL("http", ip, port, "/rpc"));

        var newHeaders = new HashMap<String, String>();
        newHeaders.put("Content-Type", "application/json");
        client.setHeaders(newHeaders);

        return ProxyUtil.createClientProxy(CasperService.class.getClassLoader(), CasperService.class, client);
    }
}
