package com.casper.sdk.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Set;

/**
 * Top level container encapsulating information required to interact with chain.
 */
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

    // TODO REMOVE once JSON parsing working
    public Deploy() {
        this(null,null, null, null, null);
    }

    //@JsonCreator
    public Deploy(final Digest hash,
                  final DeployHeader header,
                  final DeployExecutable payment,
                  final DeployExecutable session,
                  final Set<DeployApproval> approvals) {
        this.hash = hash;
        this.header = header;
        this.payment = payment;
        this.session = session;
        this.approvals = approvals;
    }

    public Digest getHash() {
        return hash;
    }

    public DeployHeader getHeader() {
        return header;
    }

    public DeployExecutable getPayment() {
        return payment;
    }

    public DeployExecutable getSession() {
        return session;
    }

    public Set<DeployApproval> getApprovals() {
        return approvals;
    }

}
