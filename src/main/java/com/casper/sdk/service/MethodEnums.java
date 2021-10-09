package com.casper.sdk.service;

import com.casper.sdk.exceptions.ValueNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ENUM that provides the methods to extract the requested key from the returned json
 */
public enum MethodEnums {

    ACCOUNT_INFO {
        @Override
        public String getValue(final String result) throws ValueNotFoundException {
            try {
                JsonNode node = new ObjectMapper().readTree(result);
                return node.get("result").toString();
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
                return node.get("result").get("state_root_hash").textValue();
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
                return node.get("result").get("stored_value").get("Account").get("main_purse").textValue();
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
                return node.get("result").get("balance_value").textValue();
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
                return node.get("result").toPrettyString();
            } catch (Exception e) {
                throw new ValueNotFoundException("auction_state not found");
            }
        }
    },
    INFO_GET_PEERS {
        @Override
        public String getValue(final String result) throws ValueNotFoundException {
            try {
                final JsonNode node = new ObjectMapper().readTree(result);
                return node.get("result").toPrettyString();
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
                return node.get("result").toPrettyString();
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
                return node.get("result").get("deploy_hash").textValue();
            } catch (Exception e) {
                throw new ValueNotFoundException("deploy_hash not found " + buildErrorMessage(node));
            }
        }
    },
    CHAIN_GET_BLOCK{
        @Override
        public String getValue(final String result) throws ValueNotFoundException {
            try{
                final JsonNode node = new ObjectMapper().readTree(result);
                return node.get("result").toPrettyString();
            } catch (Exception e){
                throw new ValueNotFoundException("result not found");
            }
        }
    },
    CHAIN_GET_BLOCK_TRANSFERS{
        @Override
        public String getValue(final String result) throws ValueNotFoundException {
            try{
                final JsonNode node = new ObjectMapper().readTree(result);
                return node.get("result").toPrettyString();
            } catch (Exception e){
                throw new ValueNotFoundException("result not found");
            }
        }
    },
    INFO_GET_DEPLOY{
        @Override
        public String getValue(final String result) throws ValueNotFoundException {
            try{
                final JsonNode node = new ObjectMapper().readTree(result);
                return node.get("result").toPrettyString();
            } catch (Exception e){
                throw new ValueNotFoundException("result not found");
            }
        }
    },
    CHAIN_GET_ERA_INFO_BY_SWITCH_BLOCK{
        @Override
        public String getValue(final String result) throws ValueNotFoundException {
            try{
                final JsonNode node = new ObjectMapper().readTree(result);
                return node.get("result").toPrettyString();
            } catch (Exception e){
                throw new ValueNotFoundException("result not found");
            }
        }
    },
    RPC_DISCOVER {
        @Override
        public String getValue(final String result) throws ValueNotFoundException {
            try {
                final JsonNode node = new ObjectMapper().readTree(result);
                return node.get("result").toPrettyString();
            } catch (Exception e) {
                throw new ValueNotFoundException("result not found");
            }
        }
    };

    public abstract String getValue(final String result) throws ValueNotFoundException;

    public String buildErrorMessage(final JsonNode node) {
        JsonNode error = node != null ? node.get("error") : null;
        if (error != null) {
            return error.toString();
        } else {
            return "";
        }
    }
}
