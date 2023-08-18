package com.casper.sdk.e2e.utils;

import com.casper.sdk.e2e.exception.NctlCommandException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * A class that executes NCTL shell commands against a node.
 *
 * @author ian@meywood.com
 */
public class Nctl {

    private final Logger logger = LoggerFactory.getLogger(Nctl.class);

    private final String dockerName;

    public Nctl(final String dockerName) {
        this.dockerName = dockerName;
    }

    public String getAccountMainPurse(final int userId) {

        final JsonNode node = execute("view_user_account.sh", "user=" + userId, s -> {
            try {
                return new ObjectMapper().readTree(s.substring(s.indexOf("{")));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });

        final String mainPurse = JsonUtils.getJsonValue(node, "/stored_value/Account/main_purse");
        assertThat(mainPurse, is(notNullValue()));
        assertThat(mainPurse, startsWith("uref-"));
        return mainPurse;
    }

    public String getStateRootHash(final int nodeId) {
        return execute("view_chain_state_root_hash.sh", "node=" + nodeId, s ->
                s.split("=")[1].trim()
        );
    }

    public String getAccountHash(final int userId) {
        final JsonNode node = getUserAccount(userId);
        final String accountHash = JsonUtils.getJsonValue(node, "/stored_value/Account/account_hash");
        assertThat(accountHash, is(notNullValue()));
        assertThat(accountHash, startsWith("account-hash-"));
        return accountHash;
    }

    public String getAccountMerkelProof(final int userId) {
        final JsonNode node = getUserAccount(userId);
        final String merkleProof = JsonUtils.getJsonValue(node, "/merkle_proof");
        assertThat(merkleProof, is(notNullValue()));
        assertThat(merkleProof, startsWith("["));
        return merkleProof;
    }

    public JsonNode getUserAccount(final int userId) {
        return execute("view_user_account.sh", "user=" + userId, Nctl::removePreamble);
    }

    public JsonNode getNodeStatus(final int nodeId) {
        return execute("view_node_status.sh", "node=" + nodeId, Nctl::removePreamble);
    }

    static JsonNode removePreamble(final String response) {
        try {
            return new ObjectMapper().readTree(response.substring(response.indexOf("{")));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public BigInteger geAccountBalance(final String purseUref) {
        return execute("view_chain_balance.sh", "purse-uref=" + purseUref,
                s -> {
                    logger.info("**** Account balance = {}", s);
                    return new BigInteger(s.split("=")[1].trim());
                }
        );
    }

    public JsonNode getStateAuctionInfo() {
        return execute("view_chain_auction_info.sh", null);
    }

    public JsonNode getChainBlock(final String blockHash) {
        return execute("view_chain_block.sh", "block=" + blockHash);
    }

    public JsonNode getChainEraInfo() {
        return execute("view_chain_era_info.sh", null);
    }

    public JsonNode getChainBlockTransfers(final String blockHash) {
        return execute("view_chain_block_transfers.sh", "block=" + blockHash);
    }

    private List<String> buildCommand(final String shellCommand, final String params) {
        return Arrays.asList("bash", "-c", String.format(
                "docker exec -t %s /bin/bash -c 'source casper-node/utils/nctl/sh/views/%s %s'",
                dockerName,
                shellCommand,
                params != null ? params : "")
        );
    }

    public <T> T execute(final String shellCommand, final String params, final Function<String, T> responseFunction) {

        try {
            final StringBuilder response = new StringBuilder();
            final Process process = new ProcessBuilder()
                    .command(buildCommand(shellCommand, params))
                    .redirectErrorStream(true)
                    .start();

            logger.info("Executing NCTL bash command: " + String.join(" ", params));

            final ConsoleStream consoleStream = new ConsoleStream(process.getInputStream(), s -> response.append(s).append("\n"));
            final Future<?> future = Executors.newSingleThreadExecutor().submit(consoleStream);

            process.waitFor();

            if (process.exitValue() != 0) {
                throw new NctlCommandException(Integer.toString(process.exitValue()));
            }

            future.get(10, TimeUnit.SECONDS);

            return responseFunction.apply(replaceAnsiConsoleCodes(response.toString()));

        } catch (Exception e) {
            throw new NctlCommandException(e);
        }
    }

    public JsonNode execute(final String shellCommand, final String params) {
        return execute(shellCommand, params, new JsonNodeResponse());
    }

    private String replaceAnsiConsoleCodes(final String response) {
        //remove any console colour ANSI info
        return response.replaceAll("\u001B\\[[;\\d]*m", "");
    }
}
