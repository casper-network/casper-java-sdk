package com.casper.sdk.service.http.rpc;

import com.casper.sdk.Constants;
import com.casper.sdk.exceptions.ValueNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.casper.sdk.Constants.RESULT;

/**
 * ENUM that provides the methods to extract the requested key from the returned json
 */
public enum MethodEnums {

    ACCOUNT_INFO {
        @Override
        public String getValue(final String result) throws ValueNotFoundException {
            try {
                JsonNode node = new ObjectMapper().readTree(result);
                return node.get(RESULT).toString();
            } catch (JsonProcessingException e) {
                throw new ValueNotFoundException("state root hash not found");
            }
        }
    },

    STATE_ROOT_HASH {
        @Override
        public String getValue(final String result) throws ValueNotFoundException {
            try {
                final JsonNode node = new ObjectMapper().readTree(result);
                return node.get(RESULT).get("state_root_hash").textValue();
            } catch (Exception e) {
                throw new ValueNotFoundException("state root hash not found");
            }
        }
    },
    STATE_GET_ITEM {
        @Override
        public String getValue(final String result) throws ValueNotFoundException {
            JsonNode node = null;
            try {
                node = new ObjectMapper().readTree(result);
                return node.get(RESULT).get("stored_value").get("Account").get("main_purse").textValue();
            } catch (Exception e) {

                throw new ValueNotFoundException("main_purse not found " + buildErrorMessage(node));
            }
        }
    },
    STATE_GET_BALANCE {
        @Override
        public String getValue(final String result) throws ValueNotFoundException {
            try {
                final JsonNode node = new ObjectMapper().readTree(result);
                return node.get(RESULT).get("balance_value").textValue();
            } catch (Exception e) {
                throw new ValueNotFoundException("balance_value not found");
            }
        }
    },
    STATE_GET_AUCTION_INFO {
        @Override
        public String getValue(final String result) throws ValueNotFoundException {
            try {
                final JsonNode node = new ObjectMapper().readTree(result);
                return node.get(RESULT).toPrettyString();
            } catch (Exception e) {
                throw new ValueNotFoundException("auction_state not found");
            }
        }
    },
    INFO_GET_DEPLOY {
        @Override
        public String getValue(String result) throws ValueNotFoundException {
            JsonNode node = null;
            try {
                node = new ObjectMapper().readTree(result);
                return node.get(RESULT).get(Constants.DEPLOY).toPrettyString();
            } catch (Exception e) {
                throw new ValueNotFoundException("deploy not found " + buildErrorMessage(node));
            }
        }
    },

    INFO_GET_PEERS {
        @Override
        public String getValue(final String result) throws ValueNotFoundException {
            try {
                final JsonNode node = new ObjectMapper().readTree(result);
                return node.get(RESULT).toPrettyString();
            } catch (Exception e) {
                throw new ValueNotFoundException("peers not found");
            }
        }
    },

    INFO_GET_STATUS {
        @Override
        public String getValue(final String result) throws ValueNotFoundException {
            try {
                final JsonNode node = new ObjectMapper().readTree(result);
                return node.get(RESULT).toPrettyString();
            } catch (Exception e) {
                throw new ValueNotFoundException("result not found");
            }
        }
    },

    ACCOUNT_PUT_DEPLOY {
        @Override
        public String getValue(final String result) throws ValueNotFoundException {
            JsonNode node = null;
            try {
                node = new ObjectMapper().readTree(result);
                return node.get(RESULT).get("deploy_hash").textValue();
            } catch (Exception e) {
                throw new ValueNotFoundException("deploy_hash not found " + buildErrorMessage(node));
            }
        }
    },

    CHAIN_GET_BLOCK {
        @Override
        public String getValue(String result) throws ValueNotFoundException {
            try {
                final JsonNode node = new ObjectMapper().readTree(result);
                return node.get(RESULT).get(Constants.BLOCK).toPrettyString();
            } catch (Exception e) {
                throw new ValueNotFoundException("block not found");
            }
        }
    };

    public abstract String getValue(final String result) throws ValueNotFoundException;

    public String buildErrorMessage(final JsonNode node) {
        final JsonNode error = node != null ? node.get("error") : null;
        if (error != null) {
            return error.toString();
        } else {
            return "";
        }
    }
}
