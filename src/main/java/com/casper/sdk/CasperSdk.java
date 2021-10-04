package com.casper.sdk;

import com.casper.sdk.exceptions.ValueNotFoundException;
import com.casper.sdk.service.hash.HashService;
import com.casper.sdk.service.http.rpc.NodeClient;
import com.casper.sdk.service.json.JsonConversionService;
import com.casper.sdk.service.serialization.cltypes.TypesFactory;
import com.casper.sdk.service.serialization.types.ByteSerializerFactory;
import com.casper.sdk.service.serialization.util.ByteUtils;
import com.casper.sdk.service.signing.SigningService;
import com.casper.sdk.types.*;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Map;

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

    public String getAccountInfo(final PublicKey accountKey) throws Exception {
        return nodeClient.getAccountInfo(signingService.toClPublicKey(accountKey).toAccountHex());
    }


    public ContractHash getContractHash(final PublicKey accountKey) throws Exception {

        final String accountInfo = getAccountInfo(accountKey);
        //noinspection rawtypes
        final Map map = jsonConversionService.fromJson(accountInfo, Map.class);
        final Object namedKeys = map.get("named_keys");
        if (namedKeys instanceof Map) {
            //noinspection rawtypes
            final String erc20 = (String) ((Map) namedKeys).get("ERC20");
            if (erc20 != null && erc20.length() > 5) {
                return new ContractHash(ByteUtils.decodeHex(erc20.substring(5)));
            }
        }
        throw new ValueNotFoundException("'ERC20' not found in account info 'named_keys'");
    }


    public String getAccountBalance(final String accountKey) throws Exception {
        return nodeClient.getAccountBalance(accountKey);
    }

    public String getAccountMainPurseURef(final String accountKey) throws Exception {
        return nodeClient.getAccountMainPurseURef(accountKey);
    }

    public String getStateRootHash() throws Exception {
        return nodeClient.getStateRootHash();
    }

    public String getAccountHash(final String accountKey) {
        return hashService.getAccountHash(accountKey);
    }

    public String getAuctionInfo() throws Exception {
        return nodeClient.getAuctionInfo();
    }

    public String getNodeStatus() throws Exception {
        return nodeClient.getNodeStatus();
    }

    public String getNodePeers() throws Exception {
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
     * @return a  new unsigned deploy for transfer purpose
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
     * @return a new unsigned deploy
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
    public Deploy signDeploy(final Deploy deploy, final KeyPair signKeyPair) {
        return deployService.signDeploy(deploy, signKeyPair);
    }

    /**
     * Send deploy to network
     *
     * @param signedDeploy Signed deploy object
     * @return the deploy hash, ech deploy gets a unique hash. This is part of the cryptographic security of blockchain
     * technology. No two deploys will ever return the same hash.
     * @throws Exception on error putting deploy
     */
    public Digest putDeploy(final Deploy signedDeploy) throws Exception {
        return new Digest(nodeClient.putDeploy(signedDeploy));
    }

    /**
     * Loads the key pair from the provided streams
     *
     * @param publicKeyIn  the public key .pem file input stream
     * @param privateKeyIn the private key .pem file input stream
     * @return the files loaded into a AsymmetricCipherKeyPair
     */
    public KeyPair loadKeyPair(final InputStream publicKeyIn,
                               final InputStream privateKeyIn) {
        return signingService.loadKeyPair(publicKeyIn, privateKeyIn);
    }

    /**
     * Creates a new standard payment.
     *
     * @param paymentAmount the number of notes paying to execution engine
     * @return a new standard payment module bytes
     */
    public ModuleBytes standardPayment(final Number paymentAmount) {
        return deployService.standardPayment(paymentAmount);
    }

    /**
     * Creates a new Transfer to the target account of the specified amount
     *
     * @param amount the amount to transfer
     * @param target the public key of the target account
     * @param id     the optional ID name argument value
     * @return the newly created transfer
     */
    public Transfer newTransfer(final Number amount, final CLPublicKey target, final Number id) {
        return deployService.newTransfer(amount, target, id);
    }

    /**
     * Converts a Casper type object ot a JSON string
     *
     * @param deploy the type object to write
     * @return the JSON representation of the clObject
     * @throws IOException - on write error
     */
    public String deployToJson(final Deploy deploy) throws IOException {
        return jsonConversionService.toJson(deploy);
    }
}
