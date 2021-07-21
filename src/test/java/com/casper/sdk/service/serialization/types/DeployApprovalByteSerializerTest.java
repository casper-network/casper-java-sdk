package com.casper.sdk.service.serialization.types;

import com.casper.sdk.types.DeployApproval;
import com.casper.sdk.types.PublicKey;
import com.casper.sdk.types.Signature;
import com.casper.sdk.service.serialization.util.ByteUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Unit tests for the {@link DeployByteSerializer}.
 */
class DeployApprovalByteSerializerTest {

    private final ByteSerializerFactory factory = new ByteSerializerFactory();
    private List<DeployApproval> approvals;

    @BeforeEach
    void setUp() {

        final DeployApproval approval1 = new DeployApproval(
                new PublicKey("017f747b67bd3fe63c2a736739dfe40156d622347346e70f68f51c178a75ce5537"),
                new Signature("0195a68b1a05731b7014e580b4c67a506e0339a7fffeaded9f24eb2e7f78b96bdd900b9be8ca33e4552a9a619dc4fc5e4e3a9f74a4b0537c14a5a8007d62a5dc06")
        );

        final DeployApproval approval2 = new DeployApproval(
                new PublicKey("027f747b67bd3fe63c2a736739dfe40156d622347346e70f68f51c178a75ce5537"),
                new Signature("0295a68b1a05731b7014e580b4c67a506e0339a7fffeaded9f24eb2e7f78b96bdd900b9be8ca33e4552a9a619dc4fc5e4e3a9f74a4b0537c14a5a8007d62a5dc06")
        );

        approvals = List.of(approval1, approval2);
    }

    /**
     * Tests a single DeployApproval can be converted to a byte array.
     */
    @Test
    void deployApprovalToBytes() {

        final byte[] expected = ByteUtils.decodeHex(
                "017f747b67bd3fe63c2a736739dfe40156d622347346e70f68f51c178a75ce5537" +
                        "0195a68b1a05731b7014e580b4c67a506e0339a7fffeaded9f24eb2e7f78b96bdd900b9be8ca33e4552a9a619dc4fc5e4e3a9f74a4b0537c14a5a8007d62a5dc06"
        );

        final DeployApproval source = approvals.get(0);
        final byte[] bytes = factory.getByteSerializer(source).toBytes(source);

        assertThat(bytes, is(expected));
    }

    /**
     * Tests a collection of DeployApprovals can be converted to a byte array.
     */
    @Test
    void deployApprovalsToBytes() {

        final byte[] expected = ByteUtils.decodeHex(
                "02000000" // the number of elements U32 LE
                + "017f747b67bd3fe63c2a736739dfe40156d622347346e70f68f51c178a75ce5537"
                + "0195a68b1a05731b7014e580b4c67a506e0339a7fffeaded9f24eb2e7f78b96bdd900b9be8ca33e4552a9a619dc4fc5e4e3a9f74a4b0537c14a5a8007d62a5dc06"
                + "027f747b67bd3fe63c2a736739dfe40156d622347346e70f68f51c178a75ce5537"
                + "0295a68b1a05731b7014e580b4c67a506e0339a7fffeaded9f24eb2e7f78b96bdd900b9be8ca33e4552a9a619dc4fc5e4e3a9f74a4b0537c14a5a8007d62a5dc06"
        );

        final byte[] bytes = factory.getByteSerializer(approvals).toBytes(approvals);

        assertThat(bytes, is(expected));
    }
}