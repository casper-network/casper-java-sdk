package com.casper.sdk.model.event;

import com.casper.sdk.model.event.blockadded.BlockAdded;
import com.casper.sdk.model.event.deployaccepted.DeployAccepted;
import com.casper.sdk.model.event.deployexpired.DeployExpired;
import com.casper.sdk.model.event.deployprocessed.DeployProcessed;
import com.casper.sdk.model.event.fault.Fault;
import com.casper.sdk.model.event.finalitysignature.FinalitySignatureV1;
import com.casper.sdk.model.event.shutdown.Shutdown;
import com.casper.sdk.model.event.step.Step;
import com.casper.sdk.model.event.version.ApiVersion;

/**
 * The enums of the allowable data type key names
 *
 * @author ian@meywood.com
 */
public enum DataType {

    API_VERSION(ApiVersion.class),
    BLOCK_ADDED(BlockAdded.class),
    DEPLOY_ACCEPTED(DeployAccepted.class),
    DEPLOY_EXPIRED(DeployExpired.class),
    DEPLOY_PROCESSED(DeployProcessed.class),
    FAULT(Fault.class),
    FINALITY_SIGNATURE(FinalitySignatureV1.class),
    SHUTDOWN(Shutdown.class),
    STEP(Step.class);

    /** The EventData class for the data type */
    private Class<? extends EventData> dataType;

    DataType(Class<? extends EventData> dataType) {
        this.dataType = dataType;
    }

    public static DataType of(final Class dataTypeClass) {
        for (DataType dataType : DataType.values()) {
            if (dataType.dataType.equals(dataTypeClass)) {
                return dataType;
            }
        }
        return null;
    }

    public static DataType of(final String dataTypeSimpleClassName) {
        for (DataType dataType : DataType.values()) {
            if (dataType.dataType.getSimpleName().equals(dataTypeSimpleClassName)) {
                return dataType;
            }
        }
        return null;
    }

    public String getDataTypeName() {
        return this.dataType.getSimpleName();
    }
}
