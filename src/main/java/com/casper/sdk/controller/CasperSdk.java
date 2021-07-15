package com.casper.sdk.controller;

import com.casper.sdk.Properties;
import com.casper.sdk.domain.*;
import com.casper.sdk.json.JsonConversionService;
import com.casper.sdk.service.HashService;
import com.casper.sdk.service.SigningService;
import com.casper.sdk.service.http.rpc.NodeClient;
import com.casper.sdk.service.serialization.cltypes.TypesFactory;
import com.casper.sdk.service.serialization.domain.ByteSerializerFactory;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

/**
 * Entry point into the SDK Exposes all permissible methods
 */
public class CasperSdk {

    private final JsonConversionService jsonConversionService = new JsonConversionService();
    private final HashService hashService = new HashService();
    private final SigningService signingService = new SigningService();
    private final DeployService deployService = new DeployService(
            new ByteSerializerFactory(),
            hashService,
            jsonConversionService,
            signingService,
            new TypesFactory()
    );
    private final NodeClient nodeClient;

    public CasperSdk(final String url, final int port) {

        Properties.properties.put("node-url", url);
        Properties.properties.put("node-port", Integer.toString(port));

        this.nodeClient = new NodeClient(deployService, hashService, jsonConversionService);
    }

    public String getAccountInfo(final String accountKey) throws Throwable {
        return nodeClient.getAccountInfo(accountKey);
    }

    public String getAccountBalance(final String accountKey) throws Throwable {
        return nodeClient.getAccountBalance(accountKey);
    }

    public String getAccountMainPurseURef(final String accountKey) throws Throwable {
        return nodeClient.getAccountMainPurseURef(accountKey);
    }

    public String getStateRootHash() throws Throwable {
        return nodeClient.getStateRootHash();
    }

    public String getAccountHash(final String accountKey) throws NoSuchAlgorithmException {
        return hashService.getAccountHash(accountKey);
    }

    public String getAuctionInfo() throws Throwable {
        return nodeClient.getAuctionInfo();
    }

    public String getNodeStatus() throws Throwable {
        return nodeClient.getNodeStatus();
    }

    public String getNodePeers() throws Throwable {
        return nodeClient.getNodePeers();
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
        return deployService.makeDeploy(deployParams, session, payment);
    }

    /**
     * Sign the deploy with the specified signKeyPair.
     *
     * @param deploy      unsigned Deploy object
     * @param signKeyPair the keypair to sign the Deploy object
     * @return the signed deploy
     */
    public Deploy signDeploy(final Deploy deploy, final AsymmetricCipherKeyPair signKeyPair) {
        return deployService.signDeploy(deploy, signKeyPair);
    }

    /**
     * Send deploy to network
     *
     * @param signedDeploy Signed deploy object
     * @return the deploy hash, ech deploy gets a unique hash. This is part of the cryptographic security of blockchain
     * technology. No two deploys will ever return the same hash.
     */
    public Digest putDeploy(final Deploy signedDeploy) throws Throwable {
        return new Digest(nodeClient.putDeploy(signedDeploy));
    }

    /**
     * Loads the key pair from the provide streams
     *
     * @param publicKeyIn  the public key .pem file input stream
     * @param privateKeyIn the private key .pem file input stream
     * @return the files loaded into a AsymmetricCipherKeyPair
     * @throws IOException if the is a problem loading the files
     */
    public AsymmetricCipherKeyPair loadKeyPair(final InputStream publicKeyIn,
                                               final InputStream privateKeyIn) throws IOException {
        return signingService.loadKeyPair(publicKeyIn, privateKeyIn);
    }

    /**
     * Creates a new standard payment
     *
     * @param paymentAmount the number of notes paying to execution engine
     */
    public ModuleBytes standardPayment(final Number paymentAmount) {
        return deployService.standardPayment(paymentAmount);
    }

    /**
     * Creates a new Transfer to the target account of the specified ammount
     *
     * @param amount the amount to transfer
     * @param target the public key of the target account
     * @param id     the optional ID name argument value
     * @return the newly created transfer
     */
    public Transfer newTransfer(final Number amount, final PublicKey target, final Number id) {
        return deployService.newTransfer(amount, target, id);
    }

    /**
     * Converts a Casper domain object ot a JSON string
     *
     * @param deploy the domain object to write
     * @return the JSON representation of the clObject
     * @throws IOException - on write error
     */
    public String deployToJson(final Deploy deploy) throws IOException {
        return jsonConversionService.toJson(deploy);
    }
}
