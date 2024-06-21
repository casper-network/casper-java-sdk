package com.casper.sdk.model.transaction;

import com.casper.sdk.model.deploy.Approval;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Version1 of a Transaction
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionV1 extends Transaction {

    @JsonProperty("hash")
    private TransactionHash hash;
    @JsonProperty("header")
    private TransactionV1Header header;
    @JsonProperty("body")
    private TransactionV1Body body;
    @JsonProperty("approvals")
    private List<Approval> approvals;
}
