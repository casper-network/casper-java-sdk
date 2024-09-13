package com.casper.sdk.model.transaction.kind;

import com.casper.sdk.model.entity.contract.ByteCode;
import com.casper.sdk.model.storedvalue.StoredValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * A ByteCode kind
 * See {@link ByteCode}
 *
 * @author carl@stormeye.co.uk
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ByteCodeKind implements StoredValue<ByteCode> {

    @JsonProperty("ByteCode")
    private ByteCode value;

}
