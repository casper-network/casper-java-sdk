package com.casper.sdk.model.deploy;

import com.casper.sdk.model.clvalue.serde.CasperSerializableObject;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.key.PublicKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.oak3.sbs4j.SerializerBuffer;
import lombok.*;

import java.util.Date;
import java.util.List;

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
public class DeployHeader implements CasperSerializableObject {

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
    public void serialize(SerializerBuffer ser, boolean encodeType) {
        account.serialize(ser, encodeType);
        ser.writeI64(timeStamp.getTime());
        ttl.serialize(ser, encodeType);
        ser.writeI64(gasPrice);
        bodyHash.serialize(ser, encodeType);
        if (dependencies != null) {
            ser.writeI32(dependencies.size());
            for (Digest dependency : dependencies) {
                ser.writeByteArray(dependency.getDigest());
            }
        }
        ser.writeString(chainName);
    }
}
