package com.syntifi.casper.sdk.model.clvalue;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.syntifi.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.syntifi.casper.sdk.exception.CLValueDecodeException;
import com.syntifi.casper.sdk.model.clvalue.cltype.CLTypePublicKey;
import com.syntifi.casper.sdk.model.clvalue.encdec.CLValueDecoder;
import com.syntifi.casper.sdk.model.clvalue.encdec.CLValueEncoder;
import com.syntifi.casper.sdk.model.key.PublicKey;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Casper PublicKey CLValue implementation
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CLValuePublicKey extends AbstractCLValue<PublicKey, CLTypePublicKey> {
    private CLTypePublicKey clType = new CLTypePublicKey();

    @JsonSetter("cl_type")
    @ExcludeFromJacocoGeneratedReport
	protected void setJsonClType(CLTypePublicKey clType) {
        this.clType = clType;
    }

    @JsonGetter("cl_type")
    @ExcludeFromJacocoGeneratedReport
	protected String getJsonClType() {
        return this.getClType().getTypeName();
    }

    public CLValuePublicKey(PublicKey value) {
        this.setValue(value);
    }

    @Override
    public void encode(CLValueEncoder clve) throws IOException {
        clve.writePublicKey(this);
    }

    @Override
    public void decode(CLValueDecoder clvd) throws IOException, CLValueDecodeException {
        try {
            clvd.readPublicKey(this);
        } catch (NoSuchAlgorithmException e) {
            throw new CLValueDecodeException("Error decoding CLValuePublicKey", e);
        }
    }
}
