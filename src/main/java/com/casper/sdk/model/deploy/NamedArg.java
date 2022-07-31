package com.casper.sdk.model.deploy;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.cltype.AbstractCLType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.casper.sdk.exception.CLValueEncodeException;
import com.casper.sdk.exception.DynamicInstanceException;
import com.casper.sdk.model.clvalue.AbstractCLValue;
import com.casper.sdk.model.clvalue.CLValueI32;
import com.casper.sdk.model.clvalue.CLValueI64;
import com.casper.sdk.model.clvalue.CLValueOption;
import com.casper.sdk.model.clvalue.CLValuePublicKey;
import com.casper.sdk.model.clvalue.CLValueU128;
import com.casper.sdk.model.clvalue.CLValueU256;
import com.casper.sdk.model.clvalue.CLValueU32;
import com.casper.sdk.model.clvalue.CLValueU512;
import com.casper.sdk.model.clvalue.CLValueU64;
import com.casper.sdk.model.clvalue.encdec.CLValueEncoder;
import com.casper.sdk.model.clvalue.encdec.interfaces.EncodableValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;

/**
 * Named arguments to a contract
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public class NamedArg<P extends AbstractCLType> implements EncodableValue {

    /**
     * The first value in the array is the type of the arg
     */
    private String type;

    /**
     * The second value in the array is a CLValue type
     */
    private AbstractCLValue<?, P> clValue;

    @Override
    public void encode(CLValueEncoder clve, boolean encodeType)
            throws IOException, CLValueEncodeException, DynamicInstanceException, NoSuchTypeException {
        clve.writeString(type);
        if (clValue instanceof CLValueI32 || clValue instanceof CLValueU32) {
            clve.writeInt(32 / 8);
        }
        if (clValue instanceof CLValueI64 || clValue instanceof CLValueU64) {
            clve.writeInt(64 / 8);
        }
        if (clValue instanceof CLValueU128 || clValue instanceof CLValueU256 ||
                clValue instanceof CLValueU512 || clValue instanceof CLValuePublicKey){
            CLValueEncoder localEncoder = new CLValueEncoder();
            clValue.encode(localEncoder, false);
            int size = localEncoder.toByteArray().length;
            clve.writeInt(size); //removing the CLValue type byte at the end
        }
        if (clValue instanceof CLValueOption) {
            CLValueEncoder localEncoder = new CLValueEncoder();
            clValue.encode(localEncoder, false);
            int size = localEncoder.toByteArray().length;
            clve.writeInt(size); //removing the CLValue type byte at the end
        }
        clValue.encode(clve, encodeType);
    }
}
