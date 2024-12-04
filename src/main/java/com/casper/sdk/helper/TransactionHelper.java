package com.casper.sdk.helper;

import com.casper.sdk.model.clvalue.*;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.deploy.NamedArg;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.transaction.InitiatorAddr;
import com.casper.sdk.model.transaction.TransactionCategory;
import com.casper.sdk.model.transaction.TransactionV1;
import com.casper.sdk.model.transaction.TransactionV1Payload;
import com.casper.sdk.model.transaction.entrypoint.TransactionEntryPoint;
import com.casper.sdk.model.transaction.pricing.PricingMode;
import com.casper.sdk.model.transaction.scheduling.TransactionScheduling;
import com.casper.sdk.model.transaction.target.TransactionTarget;
import com.casper.sdk.model.uref.URef;
import dev.oak3.sbs4j.exception.ValueSerializationException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Utility class to help with the building of transactions.
 *
 * @author ian@meywood.com
 */
public class TransactionHelper {


    @SuppressWarnings({"unchecked", "rawtypes", "OptionalOfNullableMisuse"})
    public static <TransferTarget> TransactionV1 newTransfer(final BigInteger amount,
                                                             final URef maybeSource,
                                                             final TransferTarget target,
                                                             final Long maybeId) throws ValueSerializationException {
        final List<NamedArg<?>> namedArgs = new ArrayList<>();
        namedArgs.add(NamedArg.builder().type("amount").clValue((AbstractCLValue) new CLValueU512(amount)).build());
        namedArgs.add(NamedArg.builder()
                .type("source")
                .clValue((AbstractCLValue) new CLValueOption(Optional.ofNullable(new CLValueURef(maybeSource))))
                .build()
        );

        final AbstractCLValue clTarget;

        if (target instanceof URef) {
            clTarget = new CLValueURef((URef) target);
        } else if (target instanceof PublicKey) {
            clTarget = new CLValuePublicKey((PublicKey) target);
        } else if (target instanceof Digest) {
            clTarget = new CLValueByteArray(((Digest) target).getDigest());
        } else {
            throw new IllegalArgumentException("Invalid target type " + target);
        }

        namedArgs.add(NamedArg.builder().type("target").clValue(clTarget).build());

        if (maybeId != null) {

            namedArgs.add(new NamedArg<>("id", new CLValueOption(
                    Optional.ofNullable(maybeId != null ? new CLValueU64(BigInteger.valueOf(System.currentTimeMillis())) : null)
            )));
        }

        return TransactionV1.builder()
                //          .payload(buildTransactionPayload(initiatorAddr, new Date(), ttl, chainName, pricingMode))
                .build();
    }


    public static TransactionV1 buildTransaction(final InitiatorAddr<?> initiatorAddr,
                                                 final Ttl ttl,
                                                 final String chainName,
                                                 final PricingMode pricingMode) {

        return TransactionV1.builder()
                .payload(buildTransactionPayload(initiatorAddr, new Date(), ttl, chainName, pricingMode))
                .build();
    }

    private static TransactionV1Payload buildTransactionPayload(final List<NamedArg<?>> args,
                                                                final TransactionTarget target,
                                                                final TransactionEntryPoint entryPoint,
                                                                final TransactionCategory category,
                                                                final TransactionScheduling scheduling) {
        return TransactionV1Payload.builder()
                .args(args)
                .target(target)
                .entryPoint(entryPoint)
                .scheduling(scheduling)
                .build();
    }

    private static TransactionV1Payload buildTransactionPayload(@SuppressWarnings("rawtypes") final InitiatorAddr initiatorAddr,
                                                                final Date timestamp,
                                                                final Ttl ttl,
                                                                final String chainName,
                                                                final PricingMode pricingMode) {
        return TransactionV1Payload.builder()
                .initiatorAddr(initiatorAddr)
                .timestamp(timestamp)
                .ttl(ttl)
                .chainName(chainName)
                .pricingMode(pricingMode)
                .build();
    }

}
