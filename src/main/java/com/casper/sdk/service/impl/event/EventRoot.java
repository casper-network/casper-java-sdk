package com.casper.sdk.service.impl.event;


import com.casper.sdk.model.event.EventData;
import com.casper.sdk.model.event.blockadded.BlockAdded;
import com.casper.sdk.model.event.deployaccepted.DeployAccepted;
import com.casper.sdk.model.event.deployexpired.DeployExpired;
import com.casper.sdk.model.event.deployprocessed.DeployProcessed;
import com.casper.sdk.model.event.fault.Fault;
import com.casper.sdk.model.event.finalitysignature.FinalitySignature;
import com.casper.sdk.model.event.shutdown.Shutdown;
import com.casper.sdk.model.event.step.Step;
import com.casper.sdk.model.event.version.ApiVersion;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A root holder to allow parsing of the Pojo data as the top level element contains the actual data as a named element eg:
 *
 * <pre>{ BlockAdded: { block_hash: "6fa8d0ad18289e9a0f45993b9201560820b70d92521c5670b02dc2cf7d512ab8"}}</pre>
 *
 * @param <T> the type of the data
 *           
 * @author ian@meywood.com 
 */
final class EventRoot<T extends EventData> {

    public T data;

    @SuppressWarnings("unused")
    public EventRoot() {
        // Default constructor needed for Jackson deserialization
    }

    public EventRoot(final T data) {
        this.data = data;
    }

    /**
     * Setter for ApiVersion due to @JsonAlias not working as expected
     *
     * @param apiVersion the apiVersion to set as data
     */
    @JsonProperty("ApiVersion")
    public void setApiVersion(final ApiVersion apiVersion) {
        //noinspection unchecked
        this.data = (T) apiVersion;
    }

    /**
     * Setter for BlockAdded due to @JsonAlias not working as expected
     *
     * @param blockAdded the blockAdded to set as data
     */
    @JsonProperty("BlockAdded")
    public void setBlockAdded(final BlockAdded blockAdded) {
        //noinspection unchecked
        this.data = (T) blockAdded;
    }

    /**
     * Setter for FinalitySignature due to @JsonAlias not working as expected
     *
     * @param finalitySignature the blockAdded to set as data
     */
    @JsonProperty("FinalitySignature")
    public void setFinalitySignature(final FinalitySignature finalitySignature) {
        //noinspection unchecked
        this.data = (T) finalitySignature;
    }

    /**
     * Setter for DeployAccepted due to @JsonAlias not working as expected
     *
     * @param deployAccepted the deployAccepted to set as data
     */
    @JsonProperty("DeployAccepted")
    public void setDeployAccepted(final DeployAccepted deployAccepted) {
        //noinspection unchecked
        this.data = (T) deployAccepted;
    }

    /**
     * Setter for DeployProcessed due to @JsonAlias not working as expected
     *
     * @param deployProcessed the deployProcessed to set as data
     */
    @JsonProperty("DeployProcessed")
    public void setDeployProcessed(final DeployProcessed deployProcessed) {
        //noinspection unchecked
        this.data = (T) deployProcessed;
    }

    /**
     * Setter for DeployExpired due to @JsonAlias not working as expected
     *
     * @param deployExpired the deployExpired to set as data
     */
    @JsonProperty("DeployExpired")
    public void setDeployExpired(final DeployExpired deployExpired) {
        //noinspection unchecked
        this.data = (T) deployExpired;
    }

    /**
     * Setter for Fault due to @JsonAlias not working as expected
     *
     * @param fault the fault to set as data
     */
    @JsonProperty("Fault")
    public void setFault(final Fault fault) {
        //noinspection unchecked
        this.data = (T) fault;
    }

    /**
     * Setter for Shutdown due to @JsonAlias not working as expected
     *
     * @param shutdown the shutdown to set as data
     */
    @JsonProperty("Shutdown")
    public void setShutdown(final Shutdown shutdown) {
        //noinspection unchecked
        this.data = (T) shutdown;
    }

    /**
     * Setter for Step due to @JsonAlias not working as expected
     *
     * @param step the step to set as data
     */
    @JsonProperty("Step")
    public void setStep(final Step step) {
        //noinspection unchecked
        this.data = (T) step;
    }


    public static <E extends EventData> E getData(@SuppressWarnings("rawtypes") final EventRoot pojoData) {
        //noinspection unchecked
        return pojoData != null ? (E) pojoData.data : null;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "{" +
                "data=" + data +
                '}';
    }
}
