package com.syntifi.casper.sdk.model.storedvalue.clvalue;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.syntifi.casper.sdk.exception.CLValueDecodeException;
import com.syntifi.casper.sdk.jackson.deserializer.CLValuePublicKeyDeserializer;
import com.syntifi.casper.sdk.jackson.serializer.CLValuePublicKeySerializer;
import com.syntifi.casper.sdk.model.key.PublicKey;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueDecoder;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueEncoder;

import org.apache.commons.codec.DecoderException;

/**
 * Casper PublicKey CLValue implementation
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
@JsonSerialize(using = CLValuePublicKeySerializer.class)
@JsonDeserialize(using = CLValuePublicKeyDeserializer.class)
public class CLValuePublicKey extends AbstractCLValue<PublicKey> {
    public CLValuePublicKey() {
        this(null);
    }

    public CLValuePublicKey(PublicKey value) {
        super(value, new CLType(CLTypeData.PUBLIC_KEY, null));
    }

    @Override
    public void encode(CLValueEncoder clve) throws IOException {
        clve.writePublicKey(this);
    }

    @Override
    public void decode(CLValueDecoder clvd) throws IOException, CLValueDecodeException {
        try {
            clvd.readPublicKey(this);
        } catch (DecoderException | NoSuchAlgorithmException e) {
            throw new CLValueDecodeException("Error decoding CLValuePublicKey", e);
        }
    }
}
