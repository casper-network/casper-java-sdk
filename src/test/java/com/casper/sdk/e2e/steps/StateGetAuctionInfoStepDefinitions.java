package com.casper.sdk.e2e.steps;

import com.casper.sdk.e2e.utils.CasperClientProvider;
import com.casper.sdk.exception.CasperClientException;
import com.casper.sdk.identifier.block.HashBlockIdentifier;
import com.casper.sdk.identifier.block.HeightBlockIdentifier;
import com.casper.sdk.model.auction.AuctionData;
import com.casper.sdk.model.bid.JsonBids;
import com.casper.sdk.model.block.JsonBlockData;
import com.casper.sdk.model.era.JsonEraValidators;
import com.casper.sdk.model.era.JsonValidatorWeight;
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

import java.math.BigInteger;

import static com.casper.sdk.e2e.steps.StepConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Step definitions for the state_get_auction_info RCP method cucumber test.
 *
 * @author ian@meywood.com
 */
public class StateGetAuctionInfoStepDefinitions {

    private final ContextMap contextMap = ContextMap.getInstance();
    private final Logger logger = LoggerFactory.getLogger(StateGetAuctionInfoStepDefinitions.class);
    private final CasperService casperService = CasperClientProvider.getInstance().getCasperService();
    private final TestProperties testProperties = new TestProperties();
    private final SimpleRcpClient simpleRcpClient = new SimpleRcpClient(testProperties.getHostname(), testProperties.getRcpPort());

    @Given("that the state_get_auction_info RPC method is invoked by hash block identifier")
    public void thatTheState_get_auction_infoRPCMethodIsInvoked() throws Exception {
        logger.info("Given that the state_get_auction_info RPC method is invoked by hash block identifier");
        final JsonBlockData block = casperService.getBlock();
        final String parentHash = block.getBlock().getHeader().getParentHash().toString();
        contextMap.put("parentHash", parentHash);

        final JsonNode auctionInfoByHash = simpleRcpClient.getAuctionInfoByHash(parentHash).at("/result");
        assertThat(auctionInfoByHash, is(notNullValue()));
        contextMap.put(STATE_AUCTION_INFO_JSON, auctionInfoByHash);

        // We have to compare to current block (which may change) as nctl does not allow to spe which block to use
        // The only way to resole this is to use curl and compare against the JSON.
        final AuctionData auctionData = casperService.getStateAuctionInfo(new HashBlockIdentifier(parentHash));
        contextMap.put(STATE_GET_AUCTION_INFO_RESULT, auctionData);
    }


    @Given("that the state_get_auction_info RPC method is invoked by height block identifier")
    public void thatTheState_get_auction_infoRPCMethodIsInvokedByHeightBlockIdentifier() throws Exception {
        logger.info("Given that the state_get_auction_info RPC method is invoked by height block identifier");

        final JsonBlockData currentBlock = casperService.getBlock();
        final String parentHash = currentBlock.getBlock().getHeader().getParentHash().toString();
        final JsonBlockData block = casperService.getBlock(new HashBlockIdentifier(parentHash));

        final JsonNode stateAuctionInfoJson = simpleRcpClient.getAuctionInfoByHash(parentHash).at("/result");
        assertThat(stateAuctionInfoJson, is(notNullValue()));
        contextMap.put(STATE_AUCTION_INFO_JSON, stateAuctionInfoJson);

        final AuctionData auctionData = casperService.getStateAuctionInfo(new HeightBlockIdentifier(block.getBlock().getHeader().getHeight()));
        contextMap.put(STATE_GET_AUCTION_INFO_RESULT, auctionData);
    }

    @Then("a valid state_get_auction_info_result is returned")
    public void aValidState_get_auction_info_resultIsReturned() {
        logger.info("Given a valid state_get_auction_info_result is returned");
        final AuctionData auctionData = contextMap.get(STATE_GET_AUCTION_INFO_RESULT);
        assertThat(auctionData, is(notNullValue()));
    }

    @And("the state_get_auction_info_result has and api version of {string}")
    public void theState_get_auction_info_resultHasAndApiVersionOf(String apiVersion) {
        logger.info("And the state_get_auction_info_result has and api version of {}", apiVersion);
        final AuctionData auctionData = contextMap.get(STATE_GET_AUCTION_INFO_RESULT);
        assertThat(auctionData.getApiVersion(), is(apiVersion));
    }

    @And("the state_get_auction_info_result action_state has a valid state root hash")
    public void theState_get_auction_info_resultAction_stateHasAValidStateRootHash() {
        final AuctionData auctionData = contextMap.get(STATE_GET_AUCTION_INFO_RESULT);
        final JsonNode jsonNode = contextMap.get(STATE_AUCTION_INFO_JSON);
        final String expectedStateRootHash = jsonNode.at("/auction_state/state_root_hash").asText();
        assertThat(auctionData.getAuctionState().getStateRootHash(), is(expectedStateRootHash));
    }

    @And("the state_get_auction_info_result action_state has a valid height")
    public void theState_get_auction_info_resultAction_stateHasAValidHeight() {
        final AuctionData auctionData = contextMap.get(STATE_GET_AUCTION_INFO_RESULT);
        final JsonNode jsonNode = contextMap.get(STATE_AUCTION_INFO_JSON);
        final Long expectedBlockHeight = jsonNode.at("/auction_state/block_height").asLong();
        assertThat(auctionData.getAuctionState().getHeight(), is(expectedBlockHeight));
    }

    @And("the state_get_auction_info_result action_state has valid bids")
    public void theState_get_auction_info_resultAction_stateHasValidBids() {
        final AuctionData auctionData = contextMap.get(STATE_GET_AUCTION_INFO_RESULT);
        assertThat(auctionData.getAuctionState().getBids().size(), is(greaterThan(0)));

        final JsonNode jsonNode = contextMap.get(STATE_AUCTION_INFO_JSON);
        final JsonNode bidsJson = jsonNode.at("/auction_state/bids");
        assertThat(auctionData.getAuctionState().getBids().size(), is(bidsJson.size()));

        final String publicKey = bidsJson.at("/0/public_key").asText();
        JsonBids firstBid = auctionData.getAuctionState().getBids().get(0);
        assertThat(firstBid.getPublicKey().getAlgoTaggedHex(), is(publicKey));

        final String bondingPurse = bidsJson.at("/0/bid/bonding_purse").asText();
        assertThat(firstBid.getBid().getBondingPurse().getJsonURef(), is(bondingPurse));

        final int delegationRate = bidsJson.at("/0/bid/delegation_rate").asInt();
        assertThat(firstBid.getBid().getDelegationRate(), is(delegationRate));

        final boolean inactive = bidsJson.at("/0/bid/inactive").asBoolean();
        assertThat(firstBid.getBid().isInactive(), is(inactive));

        final BigInteger stakedAmount = new BigInteger(bidsJson.at("/0/bid/staked_amount").asText());
        assertThat(firstBid.getBid().getStakedAmount(), is(stakedAmount));

        final String delegatorBondingPurse = bidsJson.at("/0/bid/delegators/0/bonding_purse").asText();
        assertThat(firstBid.getBid().getDelegators().get(0).getBondingPurse().getJsonURef(), is(delegatorBondingPurse));

        final String delegatee = bidsJson.at("/0/bid/delegators/0/delegatee").asText();
        assertThat(firstBid.getBid().getDelegators().get(0).getDelegatee().getAlgoTaggedHex(), is(delegatee));

        final String delegateePublicKey = bidsJson.at("/0/bid/delegators/0/public_key").asText();
        final PublicKey pubKey = firstBid.getBid().getDelegators().get(0).getPublicKey();
        assertThat(pubKey.getAlgoTaggedHex(), is(delegateePublicKey));

        final BigInteger delegateeStakedAmount = new BigInteger(bidsJson.at("/0/bid/delegators/0/staked_amount").asText());
        assertThat(firstBid.getBid().getDelegators().get(0).getStakedAmount(), is(delegateeStakedAmount));
    }


    @And("the state_get_auction_info_result action_state has valid era validators")
    public void theState_get_auction_info_resultAction_stateHasValidEraValidators() {
        final AuctionData auctionData = contextMap.get(STATE_GET_AUCTION_INFO_RESULT);

        assertThat(auctionData.getAuctionState().getEraValidators().size(), is(greaterThan(0)));

        final JsonNode jsonNode = contextMap.get(STATE_AUCTION_INFO_JSON);
        final JsonNode eraValidatorsJson = jsonNode.at("/auction_state/era_validators");
        assertThat(eraValidatorsJson.size(), is(greaterThan(0)));

        final JsonNode firstValidatorJson = eraValidatorsJson.at("/0");
        final JsonEraValidators firstValidator = auctionData.getAuctionState().getEraValidators().get(0);
        assertThat(firstValidator.getEraId(), is(new BigInteger(firstValidatorJson.at("/era_id").asText())));

        assertThat(firstValidator.getValidatorWeights().size(), is(greaterThan(0)));

        final JsonValidatorWeight weight = firstValidator.getValidatorWeights().get(0);
        assertThat(weight.getPublicKey().getAlgoTaggedHex(), is(firstValidatorJson.at("/validator_weights/0/public_key").asText()));

        assertThat(weight.getWeight(), is(new BigInteger(firstValidatorJson.at("/validator_weights/0/weight").asText())));
    }

    @Given("that the state_get_auction_info RPC method is invoked by an invalid block hash identifier")
    public void thatTheState_get_auction_infoRPCMethodIsInvokedByAnInvalidHeightBlockIdentifier() {
        try {
            casperService.getStateAuctionInfo(new HashBlockIdentifier("9608b4b7029a18ae35373eab879f523850a1b1fd43a3e6da774826a343af4ad2"));
        } catch (CasperClientException e) {
            contextMap.put(CLIENT_EXCEPTION, e);
        }
    }
}
