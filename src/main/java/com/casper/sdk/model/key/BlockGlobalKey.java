package com.casper.sdk.model.key;

import dev.oak3.sbs4j.DeserializerBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A `Key` under which per-block details are stored to global state.
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BlockGlobalKey extends Key {

    private BlockGlobalAddr blockGlobalAddr;

    @Override
    protected void deserializeCustom(final DeserializerBuffer deser) throws Exception {
        this.setTag(KeyTag.BLOCK_GLOBAL);
        this.setKey(deser.readByteArray(33));
        this.blockGlobalAddr = BlockGlobalAddr.getByTag(getKey()[0]);
    }
}
