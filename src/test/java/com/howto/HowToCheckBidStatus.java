package com.howto;

import com.casper.sdk.identifier.block.HashBlockIdentifier;
import com.casper.sdk.model.auction.AuctionData;
import com.casper.sdk.model.bid.JsonBids;
import com.casper.sdk.service.CasperService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HowToCheckBidStatus extends HowToMethods{

    private CasperService casperService;

    @BeforeEach
    public void init() throws IOException, URISyntaxException {
        casperService = connect();
    }

    @Test
    void checkBidStatus() {

        //Use the block hash returned from the ActivateBid method to get auction info
        //Then search the Bid results for the public key used in ActivateBid

        final AuctionData stateAuctionInfo = casperService.getStateAuctionInfo(
            new HashBlockIdentifier(
                "7f62132a052053a9929c7c5b1b870bdaecf26609c2b9cfbfd06a432eacdb6574"));

        assert stateAuctionInfo != null;

        final List<JsonBids> jsonBids = stateAuctionInfo.getAuctionState().getBids().stream()
            .filter(b -> b.getPublicKey().toString()
                .equals("017d9aa0b86413d7ff9a9169182c53f0bacaa80d34c211adab007ed4876af17077"))
            .collect(Collectors.toList());


    }
}
