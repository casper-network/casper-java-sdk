package com.casper.sdk.json;

import com.casper.sdk.domain.Digest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.casper.sdk.json.JsonTestUtils.writeToJsonString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Unit tests the {@link DigestJsonJSerializer}.
 */
class DigestJsonSerializerTest {

    private static final String JSON = "\"017f747b67bd3fe63c2a736739dfe40156d622347346e70f68f51c178a75ce55\"";

    @Test
    void serializeDigest() throws IOException {
        final String json = writeToJsonString(new Digest("017f747b67bd3fe63c2a736739dfe40156d622347346e70f68f51c178a75ce55"));
        assertThat(json, is(JSON));
    }
}