package com.casper.sdk.service.json.deserialize;

import com.casper.sdk.types.Digest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Unit tests the DigestJsonDeserializer
 */
class DigestJsonDeserializerTest {

    private static final String JSON = "\"017f747b67bd3fe63c2a736739dfe40156d622347346e70f68f51c178a75ce55\"";

    /**
     * Tests that a Digest can be deserialized from JSON
     */
    @Test
    void testDigestFromJson() throws IOException {

        final SimpleModule module = new SimpleModule();
        module.addDeserializer(Digest.class, new DigestJsonDeserializer());
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);

        final Digest digest = mapper.reader().readValue(JSON, Digest.class);
        assertThat(digest, is(new Digest("017f747b67bd3fe63c2a736739dfe40156d622347346e70f68f51c178a75ce55")));
    }
}