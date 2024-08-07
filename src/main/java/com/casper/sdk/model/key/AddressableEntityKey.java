package com.casper.sdk.model.key;

import com.casper.sdk.model.entity.EntityAddr;
import dev.oak3.sbs4j.DeserializerBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A key for an Addressable entity.
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AddressableEntityKey extends Key {

    private EntityAddr entityAddressTag;

    protected void deserializeCustom(final DeserializerBuffer deser) throws Exception {
        this.setTag(KeyTag.ADDRESSABLE_ENTITY);
        this.setKey(deser.readByteArray(33));
        this.entityAddressTag = EntityAddr.getByTag(getKey()[0]);
    }
}
