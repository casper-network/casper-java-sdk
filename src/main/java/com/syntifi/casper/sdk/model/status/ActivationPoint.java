package com.syntifi.casper.sdk.model.status;

import java.math.BigInteger;
import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.syntifi.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * The first era to which the associated protocol version applies
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
public class ActivationPoint {

    /**
     * Era ID
     */
    @JsonIgnore
    private BigInteger eraId;

    /**
     * Timestamp formatted as per RFC 3339
     */
    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date timeStamp;

    @JsonProperty("era_id")
    @ExcludeFromJacocoGeneratedReport
    protected String getJsonEraId() {
        return this.eraId.toString(10);
    }

    @JsonProperty("era_id")
    @ExcludeFromJacocoGeneratedReport
    protected void setJsonEraId(String value) {
        this.eraId = new BigInteger(value, 10);
    }
}
