package com.casper.sdk.service.serialization.domain;

import com.casper.sdk.domain.DeployApproval;
import com.casper.sdk.service.serialization.util.ByteUtils;

/**
 * Byte serializer for the a {@link DeployApproval} domain object
 */
public class DeployApprovalByteSerializer implements ByteSerializer<DeployApproval> {

    @Override
    public byte[] toBytes(final DeployApproval source) {

        return ByteUtils.concat(
                source.getSigner().getBytes(),
                source.getSignature().getBytes()
        );
    }

    @Override
    public Class<DeployApproval> getType() {
        return DeployApproval.class;
    }
}
