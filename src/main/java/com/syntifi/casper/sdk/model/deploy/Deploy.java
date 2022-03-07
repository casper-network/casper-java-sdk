package com.syntifi.casper.sdk.model.deploy;

import java.io.IOException;
import java.util.List;

import com.syntifi.casper.sdk.exception.CLValueEncodeException;
import com.syntifi.casper.sdk.exception.DynamicInstanceException;
import com.syntifi.casper.sdk.exception.NoSuchTypeException;
import com.syntifi.casper.sdk.model.clvalue.encdec.CLValueEncoder;
import com.syntifi.casper.sdk.model.clvalue.encdec.interfaces.EncodableValue;
import com.syntifi.casper.sdk.model.common.Digest;
import com.syntifi.casper.sdk.model.deploy.executabledeploy.ExecutableDeployItem;

import org.bouncycastle.util.encoders.Hex;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A deploy; an item containing a smart contract along with the requester's
 * signature(s)
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Deploy implements EncodableValue {

    /**
     * Hex-encoded deploy hash
     */
    private Digest hash;

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

    /**
     * Implements Deploy encoder 
     */
    @Override
    public void encode(CLValueEncoder clve) throws IOException, CLValueEncodeException, DynamicInstanceException, NoSuchTypeException  {
        header.encode(clve);
        hash.encode(clve);
        payment.encode(clve);
        session.encode(clve);
        String e = Hex.toHexString(clve.toByteArray());
        clve.writeInt(approvals.size());
        for (Approval approval: approvals) {
            approval.encode(clve);
        }
        String f = Hex.toHexString(clve.toByteArray());
    }
}

