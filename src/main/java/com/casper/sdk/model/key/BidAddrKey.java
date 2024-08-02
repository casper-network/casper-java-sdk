package com.casper.sdk.model.key;

import dev.oak3.sbs4j.DeserializerBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BidAddrKey extends Key {

    private BidAddr bidAddr;

    protected void deserializeCustom(final DeserializerBuffer deser) throws Exception {
        this.bidAddr = BidAddr.getByTag(deser.readU8());

        final int len;
        if (BidAddr.DELEGATOR == bidAddr) {
            len = 65;
        } else if (BidAddr.CREDIT == bidAddr) {
            len = 41;
        } else {
            len = 32;
        }

        final byte[] key = new byte[len + 1];
        key[0] = bidAddr.getByteTag();
        System.arraycopy(deser.readByteArray(len), 0, key, 1, len);
        setKey(key);
    }
}
