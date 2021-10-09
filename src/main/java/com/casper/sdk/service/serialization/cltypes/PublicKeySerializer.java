package com.casper.sdk.service.serialization.cltypes;

import com.casper.sdk.service.serialization.util.ByteUtils;
import com.casper.sdk.types.Algorithm;
import com.casper.sdk.types.CLPublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.edec.BCEdDSAPublicKey;

/**
 * Converts {@link java.security.PublicKey} and {@link CLPublicKey} objects to byte arrays
 */
class PublicKeySerializer implements TypesSerializer {

    @Override
    public byte[] serialize(final Object toSerialize) {
        if (toSerialize instanceof CLPublicKey) {
            return ((CLPublicKey) toSerialize).toAccount();
        } else if (toSerialize instanceof BCECPublicKey) {
            return getSECP256K1PublicKeyRawBytes((BCECPublicKey) toSerialize);
        } else if (toSerialize instanceof BCEdDSAPublicKey) {
            return getED25519PublicKeyRawBytes((BCEdDSAPublicKey) toSerialize);
        } else {
            return new byte[0];
        }
    }

    private byte[] getSECP256K1PublicKeyRawBytes(final BCECPublicKey publicKey) {
        return ByteUtils.concat(
                ByteUtils.toByteArray(Algorithm.SECP256K1.getValue()),
                publicKey.getQ().getEncoded(true)
        );
    }

    private byte[] getED25519PublicKeyRawBytes(final BCEdDSAPublicKey publicKey) {
        return ByteUtils.concat(
                ByteUtils.toByteArray(Algorithm.ED25519.getValue()),
                publicKey.getPointEncoding()
        );
    }
}
