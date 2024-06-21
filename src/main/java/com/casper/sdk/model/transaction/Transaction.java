package com.casper.sdk.model.transaction;

import com.casper.sdk.model.deploy.Deploy;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Transaction response obtained using the info_get_transaction RPC call.
 *
 * @author ian@meywood.com
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Deploy.class, name = "Deploy"),
        @JsonSubTypes.Type(value = TransactionV1.class, name = "Version1")})
@NoArgsConstructor
@Getter
@Setter
public class Transaction {

}
