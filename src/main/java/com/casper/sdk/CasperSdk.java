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
import java.math.BigInteger;
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

        Properties.properties.put(Properties.NODE_URL, url);
        Properties.properties.put(Properties.NODE_PORT, Integer.toString(port));

        this.nodeClient = new NodeClient(deployService, hashService, jsonConversionService);
    }

    public String getAccountInfo(final PublicKey accountKey) {
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
    
    public BigInteger getAccountBalance(final PublicKey accountKey) {
        return nodeClient.getAccountBalance(signingService.toClPublicKey(accountKey).toAccountHex());
    }

    public URef getAccountMainPurseURef(final PublicKey accountKey) {
        return nodeClient.getAccountMainPurseURef(signingService.toClPublicKey(accountKey).toAccountHex());
    }

    public String getStateRootHash() {
        return nodeClient.getStateRootHash();
    }

    public String getAccountHash(final PublicKey accountKey) {
        return ByteUtils.encodeHexString(hashService.getAccountHash(this.getPublicKeyBytes(accountKey)));
    }

    public String getAccountHash(final String accountKey) {
        return hashService.getAccountHash(accountKey);
    }

    public String getAuctionInfo() {
        return nodeClient.getAuctionInfo();
    }

    public String getNodeStatus() {
        return nodeClient.getNodeStatus();
    }

    public String getNodePeers() {
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
     * @param signKeyPair the keypair to sign the deploy object
     * @return the signed deploy
     */
    public Deploy signDeploy(final Deploy deploy, final KeyPair signKeyPair) {
        return deployService.signDeploy(deploy, signKeyPair);
    }

    /**
     * Obtains deploy from the network
     *
     * @param deployHash the hash of the deploy object to obtain
     * @return the deploy
     */
    public Deploy getDeploy(final Digest deployHash) {
        return nodeClient.getDeploy(deployHash);
    }

    /**
     * Send deploy to network
     *
     * @param signedDeploy Signed deploy object
     * @return the deploy hash, ech deploy gets a unique hash. This is part of the cryptographic security of blockchain
     * technology. No two deploys will ever return the same hash.
     */
    public Digest putDeploy(final Deploy signedDeploy) {
        return new Digest(nodeClient.putDeploy(signedDeploy));
    }

    /**
     * Loads the key pair from the provided streams
     *
     * @param publicKeyIn  the public key .pem file input stream
     * @param privateKeyIn the private key .pem file input stream
     * @return the files loaded into a AsymmetricCipherKeyPair
     */
    public KeyPair loadKeyPair(final InputStream publicKeyIn, final InputStream privateKeyIn) {
        return signingService.loadKeyPair(publicKeyIn, privateKeyIn);
    }

    /**
     * Obtains the bytes of a public key in casper format with the first byte being the algorithm type identifier
     * value.
     *
     * @param publicKey the public key to extract the bytes from
     * @return the bytes
     */
    public byte[] getPublicKeyBytes(final PublicKey publicKey) {
        return signingService.toClPublicKey(publicKey).toAccount();
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

    /**
     * Creates a public key from a hex string where the first byte is the algorithm type and the following bytes the raw
     * public key bytes.
     *
     * @param publicKeyHex the public key hex
     * @return the java security public key
     */
    public PublicKey createPublicKey(final String publicKeyHex) {
        return signingService.fromClPublicKey(new CLPublicKey(publicKeyHex));
    }

    /**
     * Converts a java security public key to a Casper Labs public key
     *
     * @param publicKey the public key to convert
     * @return the Casper Labs public key
     */
    public CLPublicKey toCLPublicKey(final PublicKey publicKey) {
        return signingService.toClPublicKey(publicKey);
    }

    /**
     * Obtains the latest block info from a mode
     *
     * @return the block info JSON
     */
    public String getLatestBlockInfo() {
        return nodeClient.getLatestBlockInfo();
    }

    /**
     * Obtains a block info by the block's hash
     *
     * @param hash the has of the block info to obtain
     * @return the block info JSON
     */
    public String getBlockInfo(final Digest hash) {
        return nodeClient.getBlockInfo(hash);
    }

    /**
     * Obtains a block info by the block's height
     *
     * @param height the has of the block info to obtain
     * @return the block info JSON
     */
    public String getBlockInfoByHeight(final Number height) {
        return nodeClient.getBlockInfoByHeight(height);
    }

    /**
     * Returns on-chain block transfers information as JSON
     *
     * @return the block transfers information as JSON
     */
    public String getBlockTransfers() {
        return nodeClient.getBlockTransfers();
    }

    public String getEraInfoBySwitchBlock() {
        return nodeClient.getEraInfoBySwitchBlock();
    }

    /**
     * Obtain the RPC Schema as a JSON string
     *
     * @return the RPC schema
     */
    public String getRpcSchema() {
        return nodeClient.getRpcSchema();
    }
}
