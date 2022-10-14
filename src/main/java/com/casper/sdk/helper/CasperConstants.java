package com.casper.sdk.helper;

public enum CasperConstants {
    DEFAULT_DEPLOY_TTL((long) 30 * 60 * 1000),
    DEFAULT_GAS_PRICE(1),
    DEPLOY_TTL_MS_MAX((long) 1000 * 60 * 60 * 24),
    MAX_TRANSFER_ID(Long.MAX_VALUE),
    MIN_TRANSFER_AMOUNT_MOTES(2500000000L),
    STANDARD_PAYMENT_FOR_NATIVE_TRANSFERS((long) 1E8),
    STANDARD_PAYMENT_FOR_DELEGATION((long) 5e9),
    STANDARD_PAYMENT_FOR_DELEGATION_WITHDRAWAL((long) 5e9),
    STANDARD_PAYMENT_FOR_AUCTION_BID((long) 5e9),
    STANDARD_PAYMENT_FOR_AUCTION_BID_WITHDRAWAL((long) 5e9);

    public final long value;

    CasperConstants(long value) {
        this.value = value;
    }

}
