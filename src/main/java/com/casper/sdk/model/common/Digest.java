package com.casper.sdk.model.common;

import com.casper.sdk.model.clvalue.serde.CasperSerializableObject;
import com.casper.sdk.model.clvalue.serde.Target;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import dev.oak3.sbs4j.SerializerBuffer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.bouncycastle.util.encoders.DecoderException;
import org.bouncycastle.util.encoders.Hex;

/**
 * Digest for Hex String
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "digest")
@Builder
public class Digest implements CasperSerializableObject {
    @JsonValue
    private String digest;

    public byte[] getDigest() {
        return Hex.decode(this.digest);
    }

    public void setDigest(byte[] hash) {
        this.digest = Hex.toHexString(hash);
    }

    public static Digest digestFromBytes(byte[] bytes) {
        Digest digest = new Digest();
        digest.setDigest(bytes);
        return digest;
    }

    /**
     * Implements Digest encoder
     */
    @Override
    public void serialize(SerializerBuffer ser, Target target) {
        ser.writeByteArray(getDigest());
    }

    @Override
    public String toString() {
        return digest;
    }

    @JsonIgnore
    public boolean isValid(){
        try {
            Hex.decode(this.digest);
            return true;
        } catch (DecoderException e){
            return false;
        }
    }
}
