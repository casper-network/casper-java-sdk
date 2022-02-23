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
                return getResultNode(result).toPrettyString();
            } catch (JsonProcessingException e) {
                throw new ValueNotFoundException("state root hash not found");
            }
        }
    },

    ACCOUNT_PUT_DEPLOY {
        @Override
        public String getValue(final String result) throws ValueNotFoundException {
            try {
                final JsonNode resultNode = getResultNode(result);
                return resultNode.get(Constants.DEPLOY_HASH).textValue();
            } catch (Exception e) {
                throw new ValueNotFoundException("deploy_hash not found " + buildErrorMessage(result));
            }
        }
    },

    CHAIN_GET_BLOCK {
        @Override
        public String getValue(String result) throws ValueNotFoundException {
            try {
                return getResultNode(result).get(Constants.BLOCK).toPrettyString();
            } catch (Exception e) {
                throw new ValueNotFoundException("block not found");
            }
        }
    },

    CHAIN_GET_BLOCK_TRANSFERS {
        @Override
        public String getValue(final String result) throws ValueNotFoundException {
            try {
                return getResultNode(result).toPrettyString();
            } catch (Exception e) {
                throw new ValueNotFoundException("result not found");
            }
        }
    },

    CHAIN_GET_ERA_INFO_BY_SWITCH_BLOCK {
        @Override
        public String getValue(final String result) throws ValueNotFoundException {
            try {
                return getResultNode(result).toPrettyString();
            } catch (Exception e) {
                throw new ValueNotFoundException("result not found");
            }
        }
    },

    INFO_GET_DEPLOY {
        @Override
        public String getValue(String result) throws ValueNotFoundException {
            JsonNode resultNode = null;
            try {
                resultNode = getResultNode(result);
                return resultNode.get(Constants.DEPLOY).toPrettyString();
            } catch (Exception e) {
                throw new ValueNotFoundException("deploy not found " + buildErrorMessage(resultNode));
            }
        }
    },

    INFO_GET_PEERS {
        @Override
        public String getValue(final String result) throws ValueNotFoundException {
            try {
                return getResultNode(result).toPrettyString();
            } catch (Exception e) {
                throw new ValueNotFoundException("peers not found");
            }
        }
    },

    INFO_GET_STATUS {
        @Override
        public String getValue(final String result) throws ValueNotFoundException {
            try {
                return getResultNode(result).toPrettyString();
            } catch (Exception e) {
                throw new ValueNotFoundException("result not found");
            }
        }
    },

    RPC_DISCOVER {
        @Override
        public String getValue(final String result) throws ValueNotFoundException {
            try {
                return getResultNode(result).toPrettyString();
            } catch (Exception e) {
                throw new ValueNotFoundException("result not found");
            }
        }
    },

    STATE_GET_BALANCE {
        @Override
        public String getValue(final String result) throws ValueNotFoundException {
            try {
                return getResultNode(result).get(Constants.BALANCE_VALUE).textValue();
            } catch (Exception e) {
                throw new ValueNotFoundException("balance_value not found");
            }
        }
    },

    STATE_GET_ITEM {
        @Override
        public String getValue(final String result) throws ValueNotFoundException {
            JsonNode resultNode = null;
            try {
                resultNode = getResultNode(result);
                return resultNode.get("stored_value").get("Account").get("main_purse").textValue();
            } catch (Exception e) {
                throw new ValueNotFoundException("main_purse not found " + buildErrorMessage(resultNode));
            }
        }
    },

    STATE_GET_AUCTION_INFO {
        @Override
        public String getValue(final String result) throws ValueNotFoundException {
            try {
                return getResultNode(result).toPrettyString();
            } catch (Exception e) {
                throw new ValueNotFoundException("auction_state not found");
            }
        }
    },

    STATE_ROOT_HASH {
        @Override
        public String getValue(final String result) throws ValueNotFoundException {
            try {
                return getResultNode(result).get(Constants.STATE_ROOT_HASH).textValue();
            } catch (Exception e) {
                throw new ValueNotFoundException("state root hash not found");
            }
        }
    };

    abstract String getValue(final String result) throws ValueNotFoundException;

    JsonNode getResultNode(final String result) throws JsonProcessingException {
        final JsonNode node = new ObjectMapper().readTree(result);
        return node.get(RESULT);
    }

    String buildErrorMessage(final String result) {

        try {
            final JsonNode node = new ObjectMapper().readTree(result);
            final JsonNode error = node != null ? node.get("error") : null;
            if (error != null) {
                return error.toString();
            } else {
                return Constants.EMPTY_STRING;
            }
        } catch (JsonProcessingException e) {
            return Constants.EMPTY_STRING;
        }

    }

    String buildErrorMessage(final JsonNode node) {
        final JsonNode error = node != null ? node.get("error") : null;
        if (error != null) {
            return error.toString();
        } else {
            return Constants.EMPTY_STRING;
        }
    }
}
