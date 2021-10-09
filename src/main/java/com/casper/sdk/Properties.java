package com.casper.sdk;

import java.util.HashMap;
import java.util.Map;

/**
 * Project properties file
 */
public class Properties {

    public static final String CHAIN_GET_STATE_ROOT_HASH = "chain_get_state_root_hash";
    public static final String STATE_GET_ITEM = "state_get_item";
    public static final String STATE_GET_BALANCE = "state_get_balance";
    public static final String STATE_GET_AUCTION_INFO = "state_get_auction_info";
    public static final String INFO_GET_PEERS = "info_get_peers";
    public static final String INFO_GET_STATUS = "info_get_status";
    public static final String ACCOUNT_PUT_DEPLOY = "account_put_deploy";

    public static final Map<String, String> properties = new HashMap<>();
}
