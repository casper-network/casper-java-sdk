package com.casper.sdk.controller;

import com.casper.sdk.Properties;
import com.casper.sdk.domain.*;
import com.casper.sdk.json.JsonConversionService;
import com.casper.sdk.service.HashService;
import com.casper.sdk.service.QueryService;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

/**
 * Entry point into the SDK Exposes all permissible methods
 */
public class CasperSdk {

    private final QueryService queryService;
    private final JsonConversionService jsonConversionService;
    private final HashService hashService;

    public CasperSdk(final String url, final String port) {
        Properties.properties.put("node-url", url);
        Properties.properties.put("node-port", port);
        this.queryService = new QueryService();
        this.jsonConversionService = new JsonConversionService();
        this.hashService = HashService.getInstance();
    }

    public String getAccountInfo(final String accountKey) throws Throwable {
        return queryService.getAccountInfo(accountKey);
    }

    public String getAccountBalance(final String accountKey) throws Throwable {
        return queryService.getAccountBalance(accountKey);
    }

    public String getAccountMainPurseURef(final String accountKey) throws Throwable {
        return queryService.getAccountMainPurseURef(accountKey);
    }

    public String getStateRootHash() throws Throwable {
        return queryService.getStateRootHash();
    }

    public String getAccountHash(final String accountKey) throws NoSuchAlgorithmException {
        return hashService.getAccountHash(accountKey);
    }

    public String getAuctionInfo() throws Throwable {
        return queryService.getAuctionInfo();
    }

    public String getNodeStatus() throws Throwable {
        return queryService.getNodeStatus();
    }

    public String getNodePeers() throws Throwable {
        return queryService.getNodePeers();
    }

    /**
     * Converts a JSON object to a {@link Deploy}.
     *
     * @param jsonStream the input stream to read the JSON object from
     * @return a new {@link Deploy}
     * @throws IOException - if there was an error reading the stream
     */
    public Deploy deployFromJson(final InputStream jsonStream) throws IOException {
        return this.jsonConversionService.fromJson(jsonStream, Deploy.class);
    }

    /**
     * Converts a JSON object to a {@link Deploy}.
     *
     * @param json the input JSON to read t
     * @return a new {@link Deploy}
     * @throws IOException - if there was an error reading the json
     */
    public Deploy deployFromJson(final String json) throws IOException {
        return this.jsonConversionService.fromJson(json, Deploy.class);
    }


    /**
     * Construct new unsigned deploy for transfer purpose.
     *
     * @param deployParams the deployment parameters
     * @param session      the  session/transfer
     * @param payment      the payment
     */
    public Deploy makeTransferDeploy(final DeployParams deployParams,
                                     final DeployExecutable session,
                                     final DeployExecutable payment) {
        if (session instanceof Transfer) {
            return this.makeDeploy(deployParams, session, payment);
        } else {
            throw new IllegalArgumentException("The session is not a Transfer ExecutableDeployItem");
        }
    }

    /**
     * Construct new unsigned deploy.
     *
     * @param deployParams the deployment parameters
     * @param session      the  session/transfer
     * @param payment      the payment
     */
    public Deploy makeDeploy(final DeployParams deployParams,
                             final DeployExecutable session,
                             final DeployExecutable payment) {
        return DeployUtil.makeDeploy(deployParams, session, payment);
    }

    /**
     * Sign the deploy with the specified signKeyPair.
     *
     * @param deploy      unsigned Deploy object
     * @param signKeyPair the keypair to sign the Deploy object
     * @return the signed deploy
     */
    public Deploy signDeploy(final Deploy deploy, final KeyPair signKeyPair) {
        return DeployUtil.signDeploy(deploy, signKeyPair);
    }

    /**
     * Send deploy to network
     *
     * @param signedDeploy Signed deploy object
     * @return the deploy hash, ech deploy gets a unique hash. This is part of the cryptographic security of blockchain
     * technology. No two deploys will ever return the same hash.
     */
    public Digest putDeploy(final Deploy signedDeploy) throws Throwable {
        return new Digest(queryService.putDeploy(signedDeploy));
    }
}
