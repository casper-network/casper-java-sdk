package com.casper.sdk.e2e.steps;

import com.casper.sdk.e2e.utils.CasperClientProvider;
import com.casper.sdk.identifier.block.HashBlockIdentifier;
import com.casper.sdk.model.block.JsonBlockData;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.deploy.Delegator;
import com.casper.sdk.model.deploy.SeigniorageAllocation;
import com.casper.sdk.model.deploy.Validator;
import com.casper.sdk.model.era.EraInfoData;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.service.CasperService;
import com.fasterxml.jackson.databind.JsonNode;
import com.casper.sdk.e2e.utils.ContextMap;
import com.casper.sdk.e2e.utils.SimpleRcpClient;
import com.casper.sdk.e2e.utils.TestProperties;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Step Definitions for Era Summary Cucumber Tests.
 */
public class EraStepDefinitions {

    private final ContextMap contextMap = ContextMap.getInstance();
    private final Logger logger = LoggerFactory.getLogger(EraStepDefinitions.class);
    public final CasperService casperService = CasperClientProvider.getInstance().getCasperService();
    private final TestProperties testProperties = new TestProperties();
    private final SimpleRcpClient simpleRcpClient = new SimpleRcpClient(testProperties.getHostname(), testProperties.getRcpPort());

    @Given("that the era summary is requested via the sdk")
    public void thatTheEraSummaryIsRequested() {

        logger.info("that the era summary is requested via the sdk");

        final JsonBlockData block = casperService.getBlock();
        assertThat(block, is(notNullValue()));

        contextMap.put("blockHash", block.getBlock().getHash().toString());

        final EraInfoData eraSummary = casperService.getEraSummary(new HashBlockIdentifier(contextMap.get("blockHash")));
        assertThat(block, is(notNullValue()));

        contextMap.put("eraSummary", eraSummary);
    }

    @Then("request the era summary via the node")
    public void requestTheEraSummaryViaTheNode() throws Exception {

        final JsonNode nodeEraSummary = simpleRcpClient.getEraSummary(contextMap.get("blockHash"));

        contextMap.put("nodeEraSummary", nodeEraSummary.get("result").get("era_summary"));
    }

    @And("the block hash of the returned era summary is equal to the block hash of the test node era summary")
    public void theBlockHashOfTheReturnedEraSummaryIsEqualToTheBlockHashOfTheTestNodeEraSummary() {

        logger.info("And the block hash of the returned era summary is equal to the block hash of the test node era summary");

        final EraInfoData eraSummary = contextMap.get("eraSummary");
        final JsonNode nodeEraSummary = contextMap.get("nodeEraSummary");
        final String blockHash = nodeEraSummary.get("block_hash").textValue();

        assertThat(blockHash, is(eraSummary.getEraSummary().getBlockHash()));
    }

    @And("the era of the returned era summary is equal to the era of the returned test node era summary")
    public void theEraOfTheReturnedEraSummaryIsEqualToTheEraOfTheReturnedTestNodeEraSummary() {

        logger.info("And the era of the returned era summary is equal to the era of the returned test node era summary");

        final EraInfoData eraSummary = contextMap.get("eraSummary");
        final JsonNode nodeEraSummary = contextMap.get("nodeEraSummary");
        final Long eraId = nodeEraSummary.get("era_id").asLong();

        assertThat(eraId, is(eraSummary.getEraSummary().getEraId()));
    }

    @And("the merkle proof of the returned era summary is equal to the merkle proof of the returned test node era summary")
    public void theMerkleProofOfTheReturnedEraSummaryIsEqualToTheMerkleProofOfTheReturnedTestNodeEraSummary() {

        logger.info("And the merkle proof of the returned era summary is equal to the merkle proof of the returned test node era summary");

        final EraInfoData eraSummary = contextMap.get("eraSummary");
        final JsonNode nodeEraSummary = contextMap.get("nodeEraSummary");

        assertThat(eraSummary.getEraSummary().getMerkleProof(), is(nodeEraSummary.get("merkle_proof").asText()));

        final Digest digest = new Digest(eraSummary.getEraSummary().getMerkleProof());
        assertThat(digest.isValid(), is(true));
    }

    @And("the state root hash of the returned era summary is equal to the state root hash of the returned test node era summary")
    public void theStateRootHashOfTheReturnedEraSummaryIsEqualToTheStateRootHashOfTheReturnedTestNodeEraSummary() {

        logger.info("And the state root hash of the returned era summary is equal to the state root hash of the returned test node era summary");

        final EraInfoData eraSummary = contextMap.get("eraSummary");
        final JsonNode nodeEraSummary = contextMap.get("nodeEraSummary");

        assertThat(nodeEraSummary.get("state_root_hash").asText(), is(eraSummary.getEraSummary().getStateRootHash()));
    }

    @And("the delegators data of the returned era summary is equal to the delegators data of the returned test node era summary")
    public void theDelegatorsDataOfTheReturnedEraSummaryIsEqualToTheDelegatorsDataOfTheReturnedTestNodeEraSummary() {

        logger.info("And the delegators data of the returned era summary is equal to the delegators data of the returned test node era summary");

        final EraInfoData eraSummary = contextMap.get("eraSummary");
        final JsonNode nodeEraSummary = contextMap.get("nodeEraSummary");

        final JsonNode allocations = nodeEraSummary.get("stored_value").get("EraInfo").get("seigniorage_allocations");

        final List<SeigniorageAllocation> delegatorsSdk = eraSummary.getEraSummary()
                .getStoredValue().getValue().getSeigniorageAllocations()
                .stream()
                .filter(q -> q instanceof Delegator)
                .map(d -> (Delegator) d)
                .collect(Collectors.toList());

        allocations.findValues("Delegator").forEach(
                d -> {
                    final List<SeigniorageAllocation> found = delegatorsSdk
                            .stream()
                            .filter(q -> getPublicKey(d.get("delegator_public_key").asText()).equals(((Delegator) q).getDelegatorPublicKey()))
                            .collect(Collectors.toList());

                    assertThat(found.isEmpty(), is(false));
                    assertThat(d.get("validator_public_key").asText(), is(((Delegator) found.get(0)).getValidatorPublicKey().toString()));
                    assertThat(d.get("amount").asText(), is(found.get(0).getAmount().toString()));
                }
        );
    }

    @And("the validators data of the returned era summary is equal to the validators data of the returned test node era summary")
    public void theValidatorsDataOfTheReturnedEraSummaryIsEqualToTheValidatorsDataOfTheReturnedTestNodeEraSummary() {

        logger.info("And the validators data of the returned era summary is equal to the validators data of the returned test node era summary");

        final EraInfoData eraSummary = contextMap.get("eraSummary");
        final JsonNode nodeEraSummary = contextMap.get("nodeEraSummary");

        final JsonNode allocations = nodeEraSummary.get("stored_value").get("EraInfo").get("seigniorage_allocations");

        final List<SeigniorageAllocation> validatorsSdk = eraSummary.getEraSummary()
                .getStoredValue().getValue().getSeigniorageAllocations()
                .stream()
                .filter(q -> q instanceof Validator)
                .map(d -> (Validator) d)
                .collect(Collectors.toList());

        allocations.findValues("Validator").forEach(
                d -> {
                    final List<SeigniorageAllocation> found = validatorsSdk
                            .stream()
                            .filter(q -> getPublicKey(d.get("validator_public_key").asText()).equals(((Validator) q).getValidatorPublicKey()))
                            .collect(Collectors.toList());

                    assertThat(found.isEmpty(), is(false));
                    assertThat(d.get("amount").asText(), is(found.get(0).getAmount().toString()));
                }
        );
    }

    private PublicKey getPublicKey(final String key) {
        try {
            final PublicKey publicKey = new PublicKey();
            publicKey.createPublicKey(key);
            return publicKey;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
