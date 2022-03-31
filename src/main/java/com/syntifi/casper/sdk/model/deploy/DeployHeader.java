package com.syntifi.casper.sdk.model.deploy;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.syntifi.casper.sdk.exception.CLValueEncodeException;
import com.syntifi.casper.sdk.exception.DynamicInstanceException;
import com.syntifi.casper.sdk.exception.NoSuchTypeException;
import com.syntifi.casper.sdk.model.clvalue.encdec.CLValueEncoder;
import com.syntifi.casper.sdk.model.clvalue.encdec.interfaces.EncodableValue;
import com.syntifi.casper.sdk.model.common.Digest;
import com.syntifi.casper.sdk.model.common.Ttl;
import com.syntifi.casper.sdk.model.key.PublicKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The header portion of a [`Deploy`](struct.Deploy.html).
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
public class DeployHeader implements EncodableValue {

    /**
     * @see PublicKey
     */
    private PublicKey account;

    /**
     * Body hash
     */
    @JsonProperty("body_hash")
    private Digest bodyHash;

    /**
     * Chain name
     */
    @JsonProperty("chain_name")
    private String chainName;

    /**
     * Dependencies
     */
    private List<Digest> dependencies;

    /**
     * Gas price
     */
    @JsonProperty("gas_price")
    private Long gasPrice;

    /**
     * Timestamp formatted as per RFC 3339
     */
    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date timeStamp;

    /**
     * Human-readable duration
     */
    private Ttl ttl;

    /**
     * Implements DeployHearder encoder
     */
    @Override
    public void encode(CLValueEncoder clve, boolean encodeType) throws IOException, CLValueEncodeException, DynamicInstanceException, NoSuchTypeException {
        account.encode(clve, encodeType);
        clve.writeLong(timeStamp.getTime());
        ttl.encode(clve, encodeType);
        clve.writeLong(gasPrice);
        bodyHash.encode(clve, encodeType);
        if (dependencies != null) {
            clve.writeInt(dependencies.size());
            for (Digest dependency : dependencies) {
                clve.write(dependency.getDigest());
            }
        }
        clve.writeString(chainName);
    }
}
