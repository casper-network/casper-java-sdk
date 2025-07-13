package com.casper.sdk.model.transaction.target;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.CasperSerializableObject;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.key.Tag;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

import java.io.IOException;

/**
 * The runtime used to execute a `Transaction`.
 * <p>
 * TODO refactor into classes as VmCasperV2 now has fields transferred_value and seed FFS
 *
 * @author ian@meywood.com
 */
@Getter
public abstract class TransactionRuntime implements CasperSerializableObject, Tag {

    protected static final int TAG_FIELD_INDEX = 0;

    private final byte tag;

    TransactionRuntime(final int tag) {
        this.tag = (byte) tag;
    }

    @JsonIgnore
    @Override
    public byte getByteTag() {
        return tag;
    }

    public abstract void toJson(final JsonGenerator gen) throws IOException;

    public static TransactionRuntime fromJson(final JsonNode json) throws NoSuchTypeException {

        if (VmCasperV1.class.getSimpleName().equals(json.textValue())) {
            return new VmCasperV1();
        } else if (json.findValue(VmCasperV2.class.getSimpleName()) != null) {
            final JsonNode transferredValue = json.findValue("transferred_value");
            final JsonNode seed = json.findValue("seed");
            return new VmCasperV2(
                    transferredValue != null ? transferredValue.longValue() : 0,
                    seed != null && !seed.isNull()? new Digest(seed.textValue()) : null
            );
        }

        throw new NoSuchTypeException(json.asText());
    }


}
