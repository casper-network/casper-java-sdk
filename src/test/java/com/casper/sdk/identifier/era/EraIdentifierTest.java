package com.casper.sdk.identifier.era;

import com.casper.sdk.identifier.block.HashBlockIdentifier;
import com.casper.sdk.identifier.block.HeightBlockIdentifier;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Unit tests for the EraIdentifier.
 *
 * @author ian@meywood.com
 */
class EraIdentifierTest {

    @Test
    void eraIdentifierJson() throws JsonProcessingException {

        String json = new ObjectMapper().writeValueAsString(IdEraIdentifier.builder().eraId(1).build());
        assertThat(json, is("{\"Era\":1}"));

        json = new ObjectMapper().writeValueAsString(BlockEraIdentifier.builder().blockIdentifier(HeightBlockIdentifier.builder().height(1L).build()).build());
        assertThat(json, is("{\"Block\":{\"Height\":1}}"));

        json = new ObjectMapper().writeValueAsString(BlockEraIdentifier.builder().blockIdentifier(HashBlockIdentifier.builder().hash("709a31cbaff23da43995e78d2209e7f5980905cf70ef850f6744b8d3cec9af13").build()).build());
        assertThat(json, is("{\"Block\":{\"Hash\":\"709a31cbaff23da43995e78d2209e7f5980905cf70ef850f6744b8d3cec9af13\"}}"));
    }
}
