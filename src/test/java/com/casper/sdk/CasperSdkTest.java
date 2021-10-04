package com.casper.sdk;

import com.casper.sdk.service.hash.HashService;
import com.casper.sdk.service.signing.SigningService;
import com.casper.sdk.service.json.JsonConversionService;
import com.casper.sdk.service.serialization.cltypes.TypesFactory;
import com.casper.sdk.service.serialization.types.ByteSerializerFactory;
import com.casper.sdk.service.serialization.util.ByteUtils;
import com.casper.sdk.types.*;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

class CasperSdkTest {

    private final DeployService deployService = new DeployService(
            new ByteSerializerFactory(),
            new HashService(),
            new JsonConversionService(),
            new SigningService(),
            new TypesFactory()
    );

    /**
     * Tests the SDK can create a transfer object
     */
    @Test
    void newTransfer() {

        final byte[] recipientPublicKey = {(byte) 108, (byte) 114, (byte) 213, (byte) 223, (byte) 102, (byte) 130,
                (byte) 189, (byte) 133, (byte) 175, (byte) 102, (byte) 195, (byte) 84, (byte) 171, (byte) 94, (byte) 88,
                (byte) 117, (byte) 242, (byte) 142, (byte) 146, (byte) 54, (byte) 0, (byte) 20, (byte) 129, (byte) 210,
                (byte) 128, (byte) 211, (byte) 127, (byte) 27, (byte) 63, (byte) 229, (byte) 50, (byte) 192};

        // The recipientPublicKey hashed to 32 bytes using ED25519
        final String targetBytes = "473535565ff7b4fdd7aa3f7a083c7f9d05b82fcba57c22339fbf50e3c5474dcb";

        final Transfer transfer = deployService.newTransfer(
                10,
                new CLPublicKey(recipientPublicKey, SignatureAlgorithm.ED25519),
                34
        );

        assertThat(transfer.getNamedArg("amount").getValue().getParsed(), is("10"));
        assertThat(transfer.getNamedArg("amount").getValue().getCLType(), is(CLType.U512));
        assertThat(transfer.getNamedArg("id").getValue().getCLType(), is(CLType.OPTION));
        assertThat(transfer.getNamedArg("id").getValue(), is(instanceOf(CLOptionValue.class)));
        assertThat(((CLOptionTypeInfo) transfer.getNamedArg("id").getValue().getCLTypeInfo()).getInnerType().getType(), is(CLType.U64));
        assertThat(transfer.getNamedArg("id").getValue().getBytes(), is(new byte[]{1, 34, 0, 0, 0, 0, 0, 0, 0}));
        assertThat(transfer.getNamedArg("target").getValue().getBytes(), is(ByteUtils.decodeHex(targetBytes)));
        assertThat(transfer.getNamedArg("target").getValue().getCLType(), is(CLType.BYTE_ARRAY));
        assertThat(((CLByteArrayInfo) transfer.getNamedArg("target").getValue().getCLTypeInfo()).getSize(), is(32));
    }
}
