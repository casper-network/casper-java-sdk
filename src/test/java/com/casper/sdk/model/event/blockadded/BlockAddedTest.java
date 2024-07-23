package com.casper.sdk.model.event.blockadded;

import com.casper.sdk.model.block.BlockBodyV2;
import com.casper.sdk.model.common.Digest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsIterableContaining.hasItem;

/**
 * Unit tests for {@link BlockAdded}
 *
 * @author ian@meywood.com
 */
class BlockAddedTest {

    @Test
    void blockAddedJson() throws IOException {

        final BlockAdded blockAdded = new ObjectMapper().readValue(getClass().getResource("/sse/block_added.json"), BlockAdded.class);
        assertThat(blockAdded.getBlockHash(), is(new Digest("941b15ba41d0a9dd8255f23a10e38505d35100f190487d1e855094c9cc3d8b2b")));
        final BlockBodyV2 body = (BlockBodyV2) blockAdded.getBlock().getBody();
        assertThat(body.getFlatTransactions(), hasItem(new Digest("6290abb0f3934e0239d27ced1b36cb2cb7cc513fc150ab9e9d764bc6bb4284a9")));


    }
}
