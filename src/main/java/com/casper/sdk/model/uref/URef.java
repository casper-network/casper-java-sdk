package com.casper.sdk.model.uref;

import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.casper.sdk.exception.DynamicInstanceException;
import com.casper.sdk.model.clvalue.CLValueURef;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import dev.oak3.sbs4j.util.ByteUtils;
import lombok.*;

import java.io.IOException;

/**
 * URef is a tuple that contains the address of the URef and the access rights
 * to that URef. The serialized representation of the URef is 33 bytes long. The
 * first 32 bytes are the byte representation of the URef address, and the last
 * byte contains the bits corresponding to the access rights of the URef.
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see CLValueURef
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class URef {

    @JsonIgnore
    byte[] address;

    @JsonIgnore
    URefAccessRight accessRight;

    public static URef fromString(String uref)
            throws IOException, DynamicInstanceException, IllegalArgumentException {
        String[] urefParts = uref.split("-");
        if (!urefParts[0].equals("uref") || urefParts.length != 3) {
            throw new IOException("Not a valid Uref");
        }
        byte[] address = ByteUtils.parseHexString(urefParts[1]);
        byte[] accessRightByte = ByteUtils.parseHexString(urefParts[2].substring(1));
        URefAccessRight accessRight = URefAccessRight
                .getTypeBySerializationTag(accessRightByte[accessRightByte.length - 1]);
        return new URef(address, accessRight);

    }

    @JsonCreator
    public void createURef(String uref) throws IOException, DynamicInstanceException, IllegalArgumentException {
        URef obj = URef.fromString(uref);
        this.accessRight = obj.getAccessRight();
        this.address = obj.getAddress();
    }

    @JsonValue
    @ExcludeFromJacocoGeneratedReport
    public String getJsonURef() {
        return "uref-" + ByteUtils.encodeHexString(this.address) + "-0"
                + ByteUtils.encodeHexString(new byte[]{this.accessRight.serializationTag});
    }
}
