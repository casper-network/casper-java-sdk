package com.syntifi.casper.sdk.model.deploy;

import java.util.List;

import com.syntifi.casper.sdk.model.deploy.executabledeploy.ExecutableDeployItem;

import lombok.Data;

/**
 * A deploy; an item containing a smart contract along with the requester's
 * signature(s)
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
public class Deploy {

    /**
     * Hex-encoded deploy hash
     */
    private String hash;

    /**
     * @see DeployHeader
     */
    private DeployHeader header;

    /**
     * @see Approval
     */
    private List<Approval> approvals;

    /**
     * @see ExecutableDeployItem
     */
    private ExecutableDeployItem payment;

    /**
     * @see ExecutableDeployItem
     */
    private ExecutableDeployItem session;
}
