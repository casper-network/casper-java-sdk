package com.casper.sdk.model.key;

import com.syntifi.crypto.key.encdec.Hex;
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

    /** Gas hold variant. */
    private BalanceHoldAddr balanceHoldAddr;
    /** The address of the purse this hold is on. */
    private byte[] urefAddr;
    /** The block time this hold was placed. */
    private BigInteger blockTime;

    @Override
    protected void fromStringCustom(final String strKey) {
        final String[] split = strKey.split("-");
        this.balanceHoldAddr = BalanceHoldAddr.getByKeyName(split[1]);
        try {
            this.deserializeCustom(new DeserializerBuffer(Hex.decode("0" + this.balanceHoldAddr.getByteTag() + split[2])));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid key: " + strKey, e);
        }
    }

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
