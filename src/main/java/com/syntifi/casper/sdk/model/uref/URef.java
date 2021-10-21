package com.syntifi.casper.sdk.model.uref;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.syntifi.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.syntifi.casper.sdk.exception.DynamicInstanceException;
import com.syntifi.casper.sdk.exception.InvalidByteStringException;
import com.syntifi.casper.sdk.model.clvalue.encdec.StringByteHelper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * URef is a tuple that contains the address of the URef and the access rights
 * to that URef. The serialized representation of the URef is 33 bytes long. The
 * first 32 bytes are the byte representation of the URef address, and the last
 * byte contains the bits corresponding to the access rights of the URef.
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 * @See CLValueURef
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class URef {

    @JsonIgnore
    byte[] address;

    @JsonIgnore
    URefAccessRight accessRight;

    public static URef fromString(String uref)
            throws IOException, DynamicInstanceException, InvalidByteStringException {
        String[] urefParts = uref.split("-");
        if (!urefParts[0].equals("uref") || urefParts.length != 3) {
            throw new IOException("Not a valid Uref");
        }
        byte[] address = StringByteHelper.hexStringToByteArray(urefParts[1]);
        byte[] accessRightByte = StringByteHelper.hexStringToByteArray(urefParts[2].substring(1));
        URefAccessRight accessRight = URefAccessRight
                .getTypeBySerializationTag(accessRightByte[accessRightByte.length - 1]);
        return new URef(address, accessRight);

    }

    @JsonCreator
    public void createURef(String uref) throws IOException, DynamicInstanceException, InvalidByteStringException {
        URef obj = URef.fromString(uref);
        this.accessRight = obj.getAccessRight();
        this.address = obj.getAddress();
    }

    @JsonValue
    @ExcludeFromJacocoGeneratedReport
    protected String getJsonURef() {
        return "uref-" + StringByteHelper.convertBytesToHex(this.address) + "-0"
                + StringByteHelper.convertBytesToHex(new byte[] { this.accessRight.serializationTag });
    }
}
