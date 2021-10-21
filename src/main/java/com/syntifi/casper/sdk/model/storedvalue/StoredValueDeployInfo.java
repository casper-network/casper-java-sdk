package com.syntifi.casper.sdk.model.storedvalue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.syntifi.casper.sdk.model.deploy.DeployInfo;

import lombok.Data;

/**
 * Stored Value for {@link DeployInfo}
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see StoredValue
 * @since 0.0.1
 */
@Data
@JsonTypeName("DeployInfo")
public class StoredValueDeployInfo implements StoredValue<DeployInfo> {
    @JsonProperty("DeployInfo")
    public DeployInfo value;
}
