package com.syntifi.casper.sdk.model.deploy;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.syntifi.casper.sdk.exception.CLValueEncodeException;
import com.syntifi.casper.sdk.exception.DynamicInstanceException;
import com.syntifi.casper.sdk.exception.NoSuchTypeException;
import com.syntifi.casper.sdk.model.clvalue.AbstractCLValue;
import com.syntifi.casper.sdk.model.clvalue.cltype.AbstractCLType;
import com.syntifi.casper.sdk.model.clvalue.encdec.CLValueEncoder;
import com.syntifi.casper.sdk.model.clvalue.encdec.interfaces.EncodableValue;

import org.bouncycastle.util.encoders.Hex;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    public void encode(CLValueEncoder clve)
            throws IOException, CLValueEncodeException, DynamicInstanceException, NoSuchTypeException {
        String a = Hex.toHexString(clve.toByteArray());
        clve.writeString(type);
        String b = Hex.toHexString(clve.toByteArray());
        //HERE
        clValue.encode(clve);
        String c = Hex.toHexString(clve.toByteArray());
        String d = Hex.toHexString(clve.toByteArray());
    }
}
