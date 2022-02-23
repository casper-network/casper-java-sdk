package com.casper.sdk;

public class Constants {

    public static final String ACCOUNT_PUT_DEPLOY = "account_put_deploy";
    public static final String AMOUNT = "amount";
    public static final String ARGS = "args";
    public static final String BALANCE_VALUE = "balance_value";
    public static final String BLOCK = "block";
    public static final String BLOCK_IDENTIFIER = "block_identifier";
    public static final String CHAIN_GET_BLOCK = "chain_get_block";
    public static final String CHAIN_GET_BLOCK_TRANSFERS = "chain_get_block_transfers";
    public static final String CHAIN_GET_ERA_INFO_BY_SWITCH_BLOCK = "chain_get_era_info_by_switch_block";
    public static final String CHAIN_GET_STATE_ROOT_HASH = "chain_get_state_root_hash";
    public static final String CREATE_DEPLOY_ARG = "create_deploy_arg";
    public static final String DELEGATION_RATE = "delegation_rate";
    public static final String DEPLOY = "deploy";
    public static final String DEPLOY_HASH = "deploy_hash";
    public static final int DEFAULT_GAS_PRICE = 1;
    public static final String DELEGATOR = "delegator";
    public static final String EMPTY_STRING = "";
    public static final String ENTRY_POINT = "entry_point";
    public static final String ERC_20 = "ERC20";
    public static final String HASH = "hash";
    public static final String HEIGHT = "Height";
    public static final String INFO_GET_DEPLOY = "info_get_deploy";
    public static final String INFO_GET_PEERS = "info_get_peers";
    public static final String INFO_GET_STATUS = "info_get_status";
    public static final String KEY = "key";
    /** Maximum value of a transfer ID. */
    public static final int MAX_TRANSFER_ID = (int) Math.pow(2, 63) - 1;
    public static final String MODULE_BYTES = "module_bytes";
    public static final String NAME = "name";
    public static final String NAMED_KEYS = "named_keys";
    public static final String PATH = "path";
    public static final String PUBLIC_KEY = "public_key";
    public static final String PURSE_UREF = "purse_uref";
    public static final String RECIPIENT = "recipient";
    public static final String RESULT = "result";
    public static final String RPC_DISCOVER = "rpc.discover";
    public static final String STATE_GET_AUCTION_INFO = "state_get_auction_info";
    public static final String STATE_GET_BALANCE = "state_get_balance";
    public static final String STATE_GET_ITEM = "state_get_item";
    public static final String STATE_ROOT_HASH = "state_root_hash";
    /** Default number of motes to pay for standard payments. */
    public static final int STANDARD_PAYMENT_FOR_NATIVE_TRANSFERS = (int) 1e8;
    /** Default number of motes to pay for standard delegation. */
    public static final int STANDARD_PAYMENT_FOR_DELEGATION = (int) 3e9;
    /** Default number of motes to pay for standard delegation withdrawal. */
    public static final int STANDARD_PAYMENT_FOR_DELEGATION_WITHDRAWAL = (int) 3e9;
    /** Default number of motes to pay for standard auction bid. */
    public static final int STANDARD_PAYMENT_FOR_AUCTION_BID = (int) 3e9;
    /** Default number of motes to pay for standard auction bid withdrawal. */
    public static final int STANDARD_PAYMENT_FOR_AUCTION_BID_WITHDRAWAL = (int) 3e9;
    public static final String TRANSFER = "transfer";
    public static final String TOKEN_DECIMALS = "token_decimals";
    public static final String TOKEN_NAME = "token_name";
    public static final String TOKEN_SYMBOL = "token_symbol";
    public static final String TOKEN_TOTAL_SUPPLYL = "token_total_supply";
    public static final String VALIDATOR = "validator";
    public static final String VERSION = "version";
    public static final String UNBOND_PURSE = "unbond_purse";
}
