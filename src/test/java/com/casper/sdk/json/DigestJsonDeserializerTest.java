package com.casper.sdk.json;

import com.casper.sdk.domain.Digest;
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

    @Test
    void testDigestFromJson() throws IOException {

        SimpleModule module = new SimpleModule();
        module.addDeserializer(Digest.class, new DigestJsonDeserializer());
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);

        Digest digest = mapper.reader().readValue("\"017f747b67bd3fe63c2a736739dfe40156d622347346e70f68f51c178a75ce5537\"", Digest.class);
        assertThat(digest, is(new Digest("017f747b67bd3fe63c2a736739dfe40156d622347346e70f68f51c178a75ce5537")));
    }
}