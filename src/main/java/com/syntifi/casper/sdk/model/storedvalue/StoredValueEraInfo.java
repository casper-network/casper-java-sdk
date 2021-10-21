package com.syntifi.casper.sdk.model.storedvalue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.syntifi.casper.sdk.model.deploy.EraInfo;

import lombok.Data;

/**
 * Stored Value for {@link EraInfo}
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see StoredValue
 * @since 0.0.1
 */
@Data
@JsonTypeName("EraInfo")
public class StoredValueEraInfo implements StoredValue<EraInfo> {
    @JsonProperty("EraInfo")
    public EraInfo value;
}
