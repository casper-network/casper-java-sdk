package com.casper.sdk.model.key;

import com.casper.sdk.exception.NoSuchKeyTagException;
import com.syntifi.crypto.key.encdec.Hex;
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


    @Override
    protected void fromStringCustom(String strKey) {
        final String[] parts = strKey.split("-");
        try {
            this.bidAddr = BidAddr.getByTag(Hex.decode(parts[parts.length-1].substring(0,2))[0]);
            this.setKey(Hex.decode(parts[parts.length-1]));
        } catch (NoSuchKeyTagException e) {
            throw new IllegalArgumentException(e);
        }
    }

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
