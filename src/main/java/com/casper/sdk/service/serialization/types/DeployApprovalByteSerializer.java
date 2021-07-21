package com.casper.sdk.service.serialization.types;

import com.casper.sdk.types.DeployApproval;
import com.casper.sdk.service.serialization.util.ByteUtils;

/**
 * Byte serializer for the a {@link DeployApproval} type object
 */
public class DeployApprovalByteSerializer implements ByteSerializer<DeployApproval> {

    @Override
    public byte[] toBytes(final DeployApproval source) {

        return ByteUtils.concat(
                source.getSigner().toAccount(),
                source.getSignature().toAccount()
        );
    }

    @Override
    public Class<DeployApproval> getType() {
        return DeployApproval.class;
    }
}
