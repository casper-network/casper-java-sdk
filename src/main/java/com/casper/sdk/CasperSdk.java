package com.casper.sdk;

import com.casper.sdk.exceptions.CasperException;
import com.casper.sdk.exceptions.ConversionException;
import com.casper.sdk.exceptions.ValueNotFoundException;
import com.casper.sdk.service.hash.HashService;
import com.casper.sdk.service.http.rpc.HttpMethods;
import com.casper.sdk.service.http.rpc.NodeClient;
import com.casper.sdk.service.json.JsonConversionService;
import com.casper.sdk.service.metrics.MetricsService;
import com.casper.sdk.service.serialization.cltypes.CLValueBuilder;
import com.casper.sdk.service.serialization.cltypes.TypesFactory;
import com.casper.sdk.service.serialization.types.ByteSerializerFactory;
import com.casper.sdk.service.serialization.util.ByteUtils;
import com.casper.sdk.service.signing.SigningService;
import com.casper.sdk.types.*;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Map;
import java.util.Random;

import static com.casper.sdk.Constants.*;

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
    private final MetricsService metricsService;

    public CasperSdk(final String url, final int port) {

        final HttpMethods httpMethods = new HttpMethods(jsonConversionService, url, port);
        this.nodeClient = new NodeClient(deployService, hashService, httpMethods);
        metricsService = new MetricsService(httpMethods);
    }

    public String getAccountInfo(final PublicKey accountKey) {
        return nodeClient.getAccountInfo(signingService.toClPublicKey(accountKey).toAccountHex());
    }

    public ContractHash getContractHash(final PublicKey accountKey) {

        final String accountInfo = getAccountInfo(accountKey);
        //noinspection rawtypes
        final Map map;
        try {
            map = jsonConversionService.fromJson(accountInfo, Map.class);
        } catch (IOException e) {
            throw new ConversionException(e);
        }
        final Object namedKeys = map.get(NAMED_KEYS);
        if (namedKeys instanceof Map) {
            //noinspection rawtypes
            final String erc20 = (String) ((Map) namedKeys).get(ERC_20);
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
     * @return a new {@link Deploy}gi
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
     * Creates a standard delegation deploy
     *
     * @param deployParams       standard parameters used when creating a deploy
     * @param amount             the  amount in motes to be delegated.
     * @param delegatorPublicKey public key of delegator.
     * @param validatorPublicKey public key of validator
     * @param wasmIn             input stream of a compiled delegate.wasm.
     * @return a standard delegation deploy
     */
    public Deploy makeValidatorDelegation(
            final DeployParams deployParams,
            final Number amount,
            final PublicKey delegatorPublicKey,
            final PublicKey validatorPublicKey,
            final InputStream wasmIn) {

        return makeDeploy(deployParams,
                new ModuleBytes(readWasm(wasmIn),
                        new DeployNamedArgBuilder()
                                .add(AMOUNT, CLValueBuilder.u512(amount))
                                .add(DELEGATOR, CLValueBuilder.publicKey(delegatorPublicKey))
                                .add(VALIDATOR, CLValueBuilder.publicKey(validatorPublicKey))
                                .build()
                ),
                this.standardPayment(STANDARD_PAYMENT_FOR_DELEGATION)
        );
    }

    /**
     * Creates a standard withdraw delegation deploy.
     *
     * @param deployParams       standard parameters used when creating a deploy
     * @param amount             amount in motes to be delegated
     * @param delegatorPublicKey public key of delegator
     * @param validatorPublicKey public key of validator
     * @param wasmIn             to compiled delegate.wasm
     * @return A standard delegation deploy.
     */
    public Deploy makeValidatorDelegationWithdrawal(final DeployParams deployParams,
                                                    final Number amount,
                                                    final PublicKey delegatorPublicKey,
                                                    final PublicKey validatorPublicKey,
                                                    final InputStream wasmIn) {

        return makeDeploy(deployParams,
                new ModuleBytes(readWasm(wasmIn),
                        new DeployNamedArgBuilder()
                                .add(AMOUNT, CLValueBuilder.u512(amount))
                                .add(DELEGATOR, CLValueBuilder.publicKey(delegatorPublicKey))
                                .add(VALIDATOR, CLValueBuilder.publicKey(validatorPublicKey))
                                .build()
                ),
                this.standardPayment(STANDARD_PAYMENT_FOR_DELEGATION_WITHDRAWAL)
        );
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
     * Loads a single public or private key from a .pem in the provided stream
     *
     * @param in  the stream for a .pem file
     * @param <T> the type of the key
     * @return the loaded key
     */
    public <T extends Key> T loadKey(final InputStream in) {
        return signingService.loadKey(in);
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
    public Transfer newTransfer(final Number amount, final PublicKey target, final Number id) {
        return deployService.newTransfer(amount, toCLPublicKey(target), id);
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
     * Obtains the node metrics as JSON
     *
     * @return the metrics JSON result
     */
    public String getNodeMetrics() {
        return metricsService.getMetrics();
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

    /**
     * Obtains the chain ero info by switch block as JSON
     *
     * @return the JSON result
     */
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

    private byte[] readWasm(final InputStream wasmIn) {
        try {
            return IOUtils.toByteArray(wasmIn);
        } catch (IOException e) {
            throw new CasperException("Error loading wasm", e);
        }
    }

    /**
     * Creates a validator auction bid deploy.
     *
     * @param deployParams       standard parameters used when creating a deploy.
     * @param amount             amount in motes to be submitted as an auction bid.
     * @param delegationRate     percentage charged to a delegator for provided service
     * @param validatorPublicKey public key of validator
     * @param wasmIn             stream of compiled delegate.wasm
     * @return a validator auction bid deploy.
     */
    public Deploy makeValidatorAuctionBid(final DeployParams deployParams,
                                          final Number amount,
                                          final int delegationRate,
                                          final PublicKey validatorPublicKey,
                                          final InputStream wasmIn) {
        return makeDeploy(
                deployParams,
                new ModuleBytes(readWasm(wasmIn),
                        new DeployNamedArgBuilder()
                                .add(AMOUNT, CLValueBuilder.u512(amount))
                                .add(DELEGATION_RATE,
                                        CLValueBuilder.u8(delegationRate))
                                .add(PUBLIC_KEY, CLValueBuilder.publicKey(validatorPublicKey))
                                .build()
                ),
                this.standardPayment(STANDARD_PAYMENT_FOR_AUCTION_BID)
        );
    }

    /**
     * Creates a native transfer deploy.
     *
     * @param deployParams  standard parameters used when creating a deploy.
     * @param amount        amount in motes to be transferred.
     * @param target        target account public key.
     * @param correlationId identifier used to correlate transfer to internal systems.
     * @return native transfer deploy.
     */
    public Deploy makeNativeTransfer(final DeployParams deployParams,
                                     final Number amount,
                                     final PublicKey target,
                                     final Number correlationId) {
        return makeDeploy(
                deployParams,
                newTransfer(amount, target, correlationId != null ? correlationId : getRandomCorrelationId()),
                standardPayment(STANDARD_PAYMENT_FOR_NATIVE_TRANSFERS)
        );
    }

    /**
     * Creates an auction bid withdraw delegation deploy.
     *
     * @param deployParams       standard parameters used when creating a deploy.
     * @param amount             amount in motes to be withdrawn from auction.
     * @param validatorPublicKey public key of validator.
     * @param wasmIn             input stream of  compiled delegate.wasm.
     * @param unbondPurse        validator's * purse to which to withdraw fund
     * @return an auction bid withdraw delegation deploy.
     */
    public Deploy makeValidatorAuctionBidWithdrawal(final DeployParams deployParams,
                                                    final Number amount,
                                                    final PublicKey validatorPublicKey,
                                                    final InputStream wasmIn,
                                                    final URef unbondPurse) {

        return makeDeploy(
                deployParams,
                new ModuleBytes(readWasm(wasmIn),
                        new DeployNamedArgBuilder()
                                .add(AMOUNT, CLValueBuilder.u32(amount))
                                .add(PUBLIC_KEY, CLValueBuilder.publicKey(validatorPublicKey))
                                .add(UNBOND_PURSE, CLValueBuilder.uRefKey(unbondPurse.getBytes()))
                                .build()),
                standardPayment(STANDARD_PAYMENT_FOR_AUCTION_BID_WITHDRAWAL)
        );
    }

    /**
     * Creates an install contract deploy
     *
     * @param deployParams     standard parameters used when creating a deploy.
     * @param payment          mount in motes to be offered as payment.
     * @param wasmIn           path to erc20.wasm file
     * @param tokenDecimals    number of decimal places within ERC-20 token to be minted.
     * @param tokenName        name of ERC-20 token to be minted.
     * @param tokenSymbol      symbol of ERC-20 token to be minted.
     * @param tokenTotalSupply total number of ERC-20 tokens to be issued
     * @return an install contract deploy
     */
    public Deploy makeInstallContract(final DeployParams deployParams,
                                      final Number payment,
                                      final InputStream wasmIn,
                                      final int tokenDecimals,
                                      final String tokenName,
                                      final String tokenSymbol,
                                      final Number tokenTotalSupply) {
        return makeDeploy(
                deployParams,
                new ModuleBytes(
                        this.readWasm(wasmIn),
                        new DeployNamedArgBuilder()
                                .add(TOKEN_DECIMALS, CLValueBuilder.u8(tokenDecimals))
                                .add(TOKEN_NAME, CLValueBuilder.string(tokenName))
                                .add(TOKEN_SYMBOL, CLValueBuilder.string(tokenSymbol))
                                .add(CREATE_DEPLOY_ARG, CLValueBuilder.u256(tokenTotalSupply))
                                .build()
                ),
                standardPayment(payment)
        );

    }

    /**
     * Creates a new invoke contract delpoy
     *
     * @param deployParams standard parameters used when creating a deploy.
     * @param payment      amount in motes to be offered as payment
     * @param amount       amount of ERC-20 tokens to be transferred to user
     * @param operatorKey  operator account key
     * @param recipient    public ket of recipient user
     * @return a new invoke contract delpoy
     */
    public Deploy makeInvokeContract(final DeployParams deployParams,
                                     final Number payment,
                                     final Number amount,
                                     final PublicKey operatorKey,
                                     final PublicKey recipient) {

        return makeDeploy(deployParams,
                new StoredContractByHash(
                        getContractHash(operatorKey),
                        TRANSFER,
                        new DeployNamedArgBuilder()
                                .add(AMOUNT, CLValueBuilder.u256(amount))
                                .add(RECIPIENT, CLValueBuilder.publicKey(recipient))
                                .build()
                ),
                standardPayment(payment)
        );
    }

    private int getRandomCorrelationId() {
        return new Random().nextInt(MAX_TRANSFER_ID - 1) + 1;
    }
}



