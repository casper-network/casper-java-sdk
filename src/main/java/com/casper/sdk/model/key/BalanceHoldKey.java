package com.casper.sdk.model.key;

import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

/**
 * A `Key` under which a hold on a purse balance is stored.
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BalanceHoldKey extends Key {

    /**  Gas hold variant. */
    private BalanceHoldAddr balanceHoldAddr;
    /**  The address of the purse this hold is on. */
    private byte[] urefAddr;
    /**  The block time this hold was placed. */
    private BigInteger blockTime;

    @Override
    protected void deserializeCustom(final DeserializerBuffer deser) throws Exception {
        this.setTag(KeyTag.BALANCE_HOLD);
        final SerializerBuffer ser = new SerializerBuffer();
        this.balanceHoldAddr = BalanceHoldAddr.getByTag(deser.readU8());
        ser.writeU8(this.balanceHoldAddr.getByteTag());
        this.urefAddr = deser.readByteArray(32);
        ser.writeByteArray(this.urefAddr);
        this.blockTime = deser.readU64();
        ser.writeU64(this.blockTime);
        this.setKey(ser.toByteArray());
    }
}
