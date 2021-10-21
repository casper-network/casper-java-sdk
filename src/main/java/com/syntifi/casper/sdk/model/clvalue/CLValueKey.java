package com.syntifi.casper.sdk.model.clvalue;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.syntifi.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.syntifi.casper.sdk.exception.CLValueDecodeException;
import com.syntifi.casper.sdk.exception.NoSuchKeyTagException;
import com.syntifi.casper.sdk.model.clvalue.cltype.CLTypeKey;
import com.syntifi.casper.sdk.model.clvalue.encdec.CLValueDecoder;
import com.syntifi.casper.sdk.model.clvalue.encdec.CLValueEncoder;
import com.syntifi.casper.sdk.model.key.Key;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Casper Key CLValue implementation
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
public class CLValueKey extends AbstractCLValue<Key, CLTypeKey> {
    private CLTypeKey clType = new CLTypeKey();

    @JsonSetter("cl_type")
    @ExcludeFromJacocoGeneratedReport
	protected void setJsonClType(CLTypeKey clType) {
        this.clType = clType;
    }

    @JsonGetter("cl_type")
    @ExcludeFromJacocoGeneratedReport
	protected String getJsonClType() {
        return this.getClType().getTypeName();
    }

    public CLValueKey(Key value) {
        this.setValue(value);
    }

    @Override
    public void encode(CLValueEncoder clve) throws IOException {
        clve.writeKey(this);
    }

    @Override
    public void decode(CLValueDecoder clvd) throws IOException, CLValueDecodeException {
        try {
            clvd.readKey(this);
        } catch (NoSuchKeyTagException e) {
            throw new CLValueDecodeException("Error decoding CLValueKey", e);
        }
    }
}
