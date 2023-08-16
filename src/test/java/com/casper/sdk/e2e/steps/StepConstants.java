package com.casper.sdk.e2e.steps;

/**
 * Constant field/key names used as step parameters.
 *
 * @author ian@meywood.com
 */
public abstract class StepConstants {

    public static final String AMOUNT = "amount";
    public static final String CLIENT_EXCEPTION = "clientException";
    public static final String DEPLOY_ACCEPTED = "deployAccepted";
    public static final String DEPLOY_RESULT = "deployResult";
    public static final String DEPLOY_TIMESTAMP = "deployTimestamp";
    public static final String EXPECTED_JSON = "expectedJson";
    public static final String EXPECTED_STATUS_DATA = "expectedStatusData";
    public static final String EXPECTED_VALIDATOR_CHANGES = "expectedValidatorChanges";
    public static final String GAS_PRICE = "gasPrice";

    public static final String GET_DEPLOY = "getDeploy";
    public static final String GLOBAL_STATE_DATA = "globalStateData";
    public static final String INFO_GET_DEPLOY = "info_get_deploy";
    public static final String LAST_BLOCK_ADDED = "lastBlockAdded";
    public static final String PEER_DATA = "peerData";
    public static final String PUBLIC_KEY_PEM = "public_key.pem";
    public static final String PUT_DEPLOY = "put-deploy";
    public static final String RECEIVER_KEY = "receiverKey";
    public static final String SECRET_KEY_PEM = "secret_key.pem";
    public static final String SENDER_KEY = "senderKey";
    public static final String SENDER_KEY_SK = "senderKeySk";
    public static final String SENDER_KEY_PK = "senderKeyPk";
    public static final String STATE_ACCOUNT_INFO = "stateAccountInfo";
    public static final String STATE_AUCTION_INFO_JSON = "stateAuctionInfoJson";
    public static final String STATE_GET_AUCTION_INFO_RESULT = "stateGetAuctionInfoResult";
    public static final String STATE_GET_BALANCE_RESULT = "stateGetBalanceResult";
    public static final String STATE_GET_DICTIONARY_ITEM = "state_get_dictionary_item";
    public static final String STATE_ROOT_HASH = "stateRootHash";
    public static final String STATUS_DATA = "statusData";
    public static final String TRANSFER_AMOUNT = "transferAmount";
    public static final String PAYMENT_AMOUNT = "paymentAmount";
    public static final String TTL = "ttl";
    public static final String VALIDATORS_CHANGES = "validatorsChanges";
    public static final String WASM_PATH = "wasmPath";

    private StepConstants() {
        // prevent construction
    }
}
