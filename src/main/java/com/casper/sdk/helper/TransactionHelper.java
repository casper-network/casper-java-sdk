package com.casper.sdk.helper;

import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.deploy.NamedArg;
import com.casper.sdk.model.transaction.*;
import com.casper.sdk.model.transaction.entrypoint.TransactionEntryPoint;
import com.casper.sdk.model.transaction.pricing.PricingMode;
import com.casper.sdk.model.transaction.scheduling.TransactionScheduling;
import com.casper.sdk.model.transaction.target.TransactionTarget;

import java.util.Date;
import java.util.List;

/**
 * Utility class to help with the building of transactions.
 *
 * @author ian@meywood.com
 */
public class TransactionHelper {

    public static TransactionV1 buildTransaction(final InitiatorAddr<?> initiatorAddr,
                                                 final Ttl ttl,
                                                 final String chainName,
                                                 final PricingMode pricingMode,
                                                 final TransactionV1Body body) {

        return TransactionV1.builder()
                .header(buildTransactionHeader(initiatorAddr, new Date(), ttl, chainName, pricingMode))
                .body(body)
                .build();
    }

    private static TransactionV1Body buildTransactionBody(final List<NamedArg<?>> args,
                                                          final TransactionTarget target,
                                                          final TransactionEntryPoint entryPoint,
                                                          final TransactionCategory category,
                                                          final TransactionScheduling scheduling) {
        return TransactionV1Body.builder()
                .args(args)
                .target(target)
                .entryPoint(entryPoint)
                .transactionCategory(category)
                .scheduling(scheduling)
                .build();
    }

    private static TransactionV1Header buildTransactionHeader(@SuppressWarnings("rawtypes") final InitiatorAddr initiatorAddr,
                                                              final Date timestamp,
                                                              final Ttl ttl,
                                                              final String chainName,
                                                              final PricingMode pricingMode) {
        return TransactionV1Header.builder()
                .initiatorAddr(initiatorAddr)
                .timestamp(timestamp)
                .ttl(ttl)
                .chainName(chainName)
                .pricingMode(pricingMode)
                .build();
    }

}
