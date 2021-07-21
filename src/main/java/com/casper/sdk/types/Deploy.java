package com.casper.sdk.types;

import com.casper.sdk.service.json.deserialize.DeployJsonDeserializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Top level container encapsulating information required to interact with chain.
 */
@JsonDeserialize(using = DeployJsonDeserializer.class)
public class Deploy {

    /** # Unique identifier. */
    private final Digest hash;
    /** Header information encapsulating various information impacting deploy processing. */
    private final DeployHeader header;
    /** Executable information passed to chain's VM for taking payment required to process session logic. */
    private final DeployExecutable payment;
    /** Executable information passed to chain's VM. */
    private final DeployExecutable session;
    /** Set of signatures approving this deploy for execution. */
    private final Set<DeployApproval> approvals;

    @JsonCreator
    public Deploy(@JsonProperty("hash") final Digest hash,
                  @JsonProperty("header") final DeployHeader header,
                  @JsonProperty("payment") final DeployExecutable payment,
                  @JsonProperty("session") final DeployExecutable session,
                  @JsonProperty("approvals") final Set<DeployApproval> approvals) {
        this.hash = hash;
        this.header = header;
        this.payment = payment;
        this.session = session;
        this.approvals = approvals != null ? approvals : new LinkedHashSet<>();
    }

    public Digest getHash() {
        return hash;
    }

    public DeployHeader getHeader() {
        return header;
    }

    public <P extends DeployExecutable> P getPayment() {
        //noinspection unchecked
        return (P) payment;
    }

    public <T extends DeployExecutable> T getSession() {
        //noinspection unchecked
        return (T) session;
    }

    public Set<DeployApproval> getApprovals() {
        return approvals;
    }

    /**
     * Indicates if the is deploy is for a transfer
     *
     * @return true if this deploy is for a transfer otherwise false
     */
    @JsonIgnore
    public boolean isTransfer() {
        return this.getSession() instanceof Transfer;
    }
}
