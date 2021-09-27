package com.syntifi.casper.sdk.model.uref;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * URef is a tuple that contains the address of the URef and the access rights 
 * to that URef. The serialized representation of the URef is 33 bytes long. 
 * The first 32 bytes are the byte representation of the URef address, and the 
 * last byte contains the bits corresponding to the access rights of the URef. 
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
    byte[] address;
    URefAccessRight accessRight;
}
