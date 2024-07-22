package com.casper.sdk.model.transaction.target;

import com.casper.sdk.model.deploy.Deploy;
import com.casper.sdk.model.transaction.AbstractTransaction;
import com.casper.sdk.model.transaction.TransactionV1;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Transaction response obtained using the info_get_transaction RPC call.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transaction {

    @JsonProperty("Deploy")
    private Deploy deploy;
    @JsonProperty("Version1")
    private TransactionV1 version1;

    public Transaction(final TransactionV1 transactionV1) {
        this.version1 = transactionV1;
    }

    public Transaction(final Deploy deploy) {
        this.deploy = deploy;
    }

    @JsonIgnore
    public <T extends AbstractTransaction> T get() {
        return (T) (version1 != null ? version1 : deploy);
    }
}
