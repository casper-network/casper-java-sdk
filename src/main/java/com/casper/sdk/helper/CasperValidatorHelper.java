package com.casper.sdk.helper;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.CLValuePublicKey;
import com.casper.sdk.model.clvalue.CLValueU512;
import com.casper.sdk.model.clvalue.CLValueU8;
import com.casper.sdk.model.clvalue.CLValueURef;
import com.casper.sdk.model.clvalue.cltype.CLTypePublicKey;
import com.casper.sdk.model.clvalue.cltype.CLTypeU512;
import com.casper.sdk.model.clvalue.cltype.CLTypeU8;
import com.casper.sdk.model.clvalue.cltype.CLTypeURef;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.deploy.Deploy;
import com.casper.sdk.model.deploy.NamedArg;
import com.casper.sdk.model.deploy.executabledeploy.ModuleBytes;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.uref.URef;
import com.syntifi.crypto.key.AbstractPrivateKey;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


/**
 * Validator helper provides methods to easily place auctions and delegate
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.5.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CasperValidatorHelper {

    /**
     * Helper method to create a validator auction bid deploy
     *
     * @param signer         private key from sender
     * @param wasmBytes      bytes of compiled delegate.wasm
     * @param bidAmount      amount in motes to be submitted as an auction bid.
     * @param delegationRate Percentage charged to a delegator for provided service.
     * @param chainName      chain name
     * @return
     * @throws NoSuchTypeException
     * @throws GeneralSecurityException
     * @throws ValueSerializationException
     */
    public static Deploy createValidatorAuctionBid(AbstractPrivateKey signer, byte[] wasmBytes, BigInteger bidAmount,
                                                   Byte delegationRate, String chainName) throws
            NoSuchTypeException, GeneralSecurityException, ValueSerializationException {
        Ttl ttl = Ttl
                .builder()
                .ttl(CasperConstants.DEFAULT_DEPLOY_TTL.value / 60 / 1000 + "m")
                .build();
        return createValidatorAuctionBid(signer, wasmBytes, bidAmount, delegationRate,
                BigInteger.valueOf(CasperConstants.STANDARD_PAYMENT_FOR_AUCTION_BID.value), chainName,
                CasperConstants.DEFAULT_GAS_PRICE.value, ttl, new Date(), new ArrayList<>());
    }

    /**
     * Helper method to create a validator auction bid deploy
     *
     * @param signer         private key from sender
     * @param wasmBytes      bytes of compiled delegate.wasm
     * @param bidAmount      amount in motes to be submitted as an auction bid.
     * @param delegationRate Percentage charged to a delegator for provided service.
     * @param paymentAmount  payment amount for processing transfers
     * @param chainName      chain name
     * @param gasPrice       gas price
     * @param ttl            time to live
     * @param date           execution date
     * @param dependencies   List of digest dependencies
     * @return
     * @throws NoSuchTypeException
     * @throws GeneralSecurityException
     * @throws ValueSerializationException
     */
    public static Deploy createValidatorAuctionBid(AbstractPrivateKey signer, byte[] wasmBytes, BigInteger bidAmount,
                                                   Byte delegationRate, BigInteger paymentAmount, String chainName,
                                                   Long gasPrice, Ttl ttl, Date date, List<Digest> dependencies)
            throws NoSuchTypeException, GeneralSecurityException, ValueSerializationException {

        PublicKey validatorKey = PublicKey.fromAbstractPublicKey(signer.derivePublicKey());
        List<NamedArg<?>> sessionArgs = new LinkedList<>();
        NamedArg<CLTypeU512> amountArg = new NamedArg<>("amount",
                new CLValueU512(bidAmount));
        sessionArgs.add(amountArg);
        NamedArg<CLTypeU8> delegationRateArg = new NamedArg<>("delegation_rate",
                new CLValueU8(delegationRate));
        sessionArgs.add(delegationRateArg);
        NamedArg<CLTypePublicKey> pkArg = new NamedArg<>("public_key",
                new CLValuePublicKey(validatorKey));
        sessionArgs.add(pkArg);

        ModuleBytes session = ModuleBytes.builder()
                .args(sessionArgs)
                .bytes(wasmBytes)
                .build();
        ModuleBytes payment = CasperDeployHelper.getPaymentModuleBytes(paymentAmount);

        return CasperDeployHelper.buildDeploy(signer, chainName, session, payment, gasPrice, ttl,
                date, dependencies);
    }


    /**
     * Helper method to create a withdraw bid deploy
     *
     * @param signer         private key from sender
     * @param wasmBytes      bytes of compiled delegate.wasm
     * @param withdrawAmount amount in motes to be submitted as an auction bid.
     * @param unboundPurse   validator's purse unforgeable reference to which to withdraw funds
     * @param chainName      chain name
     * @return
     * @throws NoSuchTypeException
     * @throws GeneralSecurityException
     * @throws ValueSerializationException
     */
    public static Deploy createValidatorAuctionBidWithdraw(AbstractPrivateKey signer, byte[] wasmBytes, BigInteger withdrawAmount,
                                                           URef unboundPurse, String chainName) throws
            NoSuchTypeException, GeneralSecurityException, ValueSerializationException {
        Ttl ttl = Ttl
                .builder()
                .ttl(CasperConstants.DEFAULT_DEPLOY_TTL.value / 60 / 1000 + "m")
                .build();
        return createValidatorAuctionBidWithdraw(signer, wasmBytes, withdrawAmount, unboundPurse,
                BigInteger.valueOf(CasperConstants.STANDARD_PAYMENT_FOR_AUCTION_BID_WITHDRAWAL.value), chainName,
                CasperConstants.DEFAULT_GAS_PRICE.value, ttl, new Date(), new ArrayList<>());
    }

    /**
     * Helper method to create a withdraw bid deploy
     *
     * @param signer         private key from sender
     * @param wasmBytes      bytes of compiled delegate.wasm
     * @param withdrawAmount amount in motes to be withdrawn from auction
     * @param unboundPurse   validator's purse unforgeable reference to which to withdraw funds
     * @param paymentAmount  payment amount for processing transfers
     * @param chainName      chain name
     * @param gasPrice       gas price
     * @param ttl            time to live
     * @param date           execution date
     * @param dependencies   List of digest dependencies
     * @return
     * @throws NoSuchTypeException
     * @throws GeneralSecurityException
     * @throws ValueSerializationException
     */
    public static Deploy createValidatorAuctionBidWithdraw(AbstractPrivateKey signer, byte[] wasmBytes, BigInteger withdrawAmount,
                                                           URef unboundPurse, BigInteger paymentAmount, String chainName,
                                                           Long gasPrice, Ttl ttl, Date date, List<Digest> dependencies)
            throws NoSuchTypeException, GeneralSecurityException, ValueSerializationException {

        PublicKey validatorKey = PublicKey.fromAbstractPublicKey(signer.derivePublicKey());
        List<NamedArg<?>> sessionArgs = new LinkedList<>();
        NamedArg<CLTypeU512> amountArg = new NamedArg<>("amount",
                new CLValueU512(withdrawAmount));
        sessionArgs.add(amountArg);
        NamedArg<CLTypePublicKey> pkArg = new NamedArg<>("public_key",
                new CLValuePublicKey(validatorKey));
        sessionArgs.add(pkArg);
        NamedArg<CLTypeURef> unboundPurseArg = new NamedArg<>("unbound_purse",
                new CLValueURef(unboundPurse));
        sessionArgs.add(unboundPurseArg);

        ModuleBytes session = ModuleBytes.builder()
                .args(sessionArgs)
                .bytes(wasmBytes)
                .build();
        ModuleBytes payment = CasperDeployHelper.getPaymentModuleBytes(paymentAmount);

        return CasperDeployHelper.buildDeploy(signer, chainName, session, payment, gasPrice, ttl,
                date, dependencies);
    }

    /**
     * Helper method to create a validator delegation deploy
     *
     * @param signer       private key from sender
     * @param wasmBytes    bytes of compiled delegate.wasm
     * @param amount       amount in motes to delegate
     * @param validatorKey validator's public key
     * @param delegatorKey delegator's public key
     * @param chainName    chain name
     * @return
     * @throws NoSuchTypeException
     * @throws GeneralSecurityException
     * @throws ValueSerializationException
     */
    public static Deploy createValidatorDelegation(AbstractPrivateKey signer, byte[] wasmBytes, BigInteger amount,
                                                   PublicKey validatorKey, PublicKey delegatorKey, String chainName) throws
            NoSuchTypeException, GeneralSecurityException, ValueSerializationException {
        Ttl ttl = Ttl
                .builder()
                .ttl(CasperConstants.DEFAULT_DEPLOY_TTL.value / 60 / 1000 + "m")
                .build();
        return createValidatorDelegation(signer, wasmBytes, amount, validatorKey, delegatorKey,
                BigInteger.valueOf(CasperConstants.STANDARD_PAYMENT_FOR_DELEGATION.value), chainName,
                CasperConstants.DEFAULT_GAS_PRICE.value, ttl, new Date(), new ArrayList<>());
    }

    /**
     * Helper method to create a validator delegation deploy
     *
     * @param signer        private key from sender
     * @param wasmBytes     bytes of compiled delegate.wasm
     * @param amount        amount in motes to delegate
     * @param validatorKey  validator's public key
     * @param delegatorKey  delegator's public key
     * @param paymentAmount payment amount for processing transfers
     * @param chainName     chain name
     * @param gasPrice      gas price
     * @param ttl           time to live
     * @param date          execution date
     * @param dependencies  List of digest dependencies
     * @return
     * @throws NoSuchTypeException
     * @throws GeneralSecurityException
     * @throws ValueSerializationException
     */
    public static Deploy createValidatorDelegation(AbstractPrivateKey signer, byte[] wasmBytes, BigInteger amount,
                                                   PublicKey validatorKey, PublicKey delegatorKey,
                                                   BigInteger paymentAmount, String chainName,
                                                   Long gasPrice, Ttl ttl, Date date, List<Digest> dependencies)
            throws NoSuchTypeException, GeneralSecurityException, ValueSerializationException {

        List<NamedArg<?>> sessionArgs = new LinkedList<>();
        NamedArg<CLTypeU512> amountArg = new NamedArg<>("amount",
                new CLValueU512(amount));
        sessionArgs.add(amountArg);
        NamedArg<CLTypePublicKey> delegatorArg = new NamedArg<>("delegator",
                new CLValuePublicKey(delegatorKey));
        sessionArgs.add(delegatorArg);
        NamedArg<CLTypePublicKey> validatorArg = new NamedArg<>("validator",
                new CLValuePublicKey(validatorKey));
        sessionArgs.add(validatorArg);

        ModuleBytes session = ModuleBytes.builder()
                .args(sessionArgs)
                .bytes(wasmBytes)
                .build();
        ModuleBytes payment = CasperDeployHelper.getPaymentModuleBytes(paymentAmount);

        return CasperDeployHelper.buildDeploy(signer, chainName, session, payment, gasPrice, ttl,
                date, dependencies);
    }

    /**
     * Helper method to create a validator delegation withdraw deploy
     *
     * @param signer       private key from sender
     * @param wasmBytes    bytes of compiled delegate.wasm
     * @param amount       amount in motes to delegate
     * @param validatorKey validator's public key
     * @param delegatorKey delegator's public key
     * @param chainName    chain name
     * @return
     * @throws NoSuchTypeException
     * @throws GeneralSecurityException
     * @throws ValueSerializationException
     */
    public static Deploy createValidatorDelegationWithdraw(AbstractPrivateKey signer, byte[] wasmBytes, BigInteger amount,
                                                           PublicKey validatorKey, PublicKey delegatorKey, String chainName) throws
            NoSuchTypeException, GeneralSecurityException, ValueSerializationException {
        Ttl ttl = Ttl
                .builder()
                .ttl(CasperConstants.DEFAULT_DEPLOY_TTL.value / 60 / 1000 + "m")
                .build();
        return createValidatorDelegationWithdraw(signer, wasmBytes, amount, validatorKey, delegatorKey,
                BigInteger.valueOf(CasperConstants.STANDARD_PAYMENT_FOR_DELEGATION_WITHDRAWAL.value), chainName,
                CasperConstants.DEFAULT_GAS_PRICE.value, ttl, new Date(), new ArrayList<>());
    }

    /**
     * Helper method to create a validator delegation withdraw deploy
     *
     * @param signer        private key from sender
     * @param wasmBytes     bytes of compiled delegate.wasm
     * @param amount        amount in motes to withdraw from delegation
     * @param validatorKey  validator's public key
     * @param delegatorKey  delegator's public key
     * @param paymentAmount payment amount for processing transfers
     * @param chainName     chain name
     * @param gasPrice      gas price
     * @param ttl           time to live
     * @param date          execution date
     * @param dependencies  List of digest dependencies
     * @return
     * @throws NoSuchTypeException
     * @throws GeneralSecurityException
     * @throws ValueSerializationException
     */
    public static Deploy createValidatorDelegationWithdraw(AbstractPrivateKey signer, byte[] wasmBytes, BigInteger amount,
                                                           PublicKey validatorKey, PublicKey delegatorKey,
                                                           BigInteger paymentAmount, String chainName,
                                                           Long gasPrice, Ttl ttl, Date date, List<Digest> dependencies)
            throws NoSuchTypeException, GeneralSecurityException, ValueSerializationException {

        List<NamedArg<?>> sessionArgs = new LinkedList<>();
        NamedArg<CLTypeU512> amountArg = new NamedArg<>("amount",
                new CLValueU512(amount));
        sessionArgs.add(amountArg);
        NamedArg<CLTypePublicKey> delegatorArg = new NamedArg<>("delegator",
                new CLValuePublicKey(delegatorKey));
        sessionArgs.add(delegatorArg);
        NamedArg<CLTypePublicKey> validatorArg = new NamedArg<>("validator",
                new CLValuePublicKey(validatorKey));
        sessionArgs.add(validatorArg);

        ModuleBytes session = ModuleBytes.builder()
                .args(sessionArgs)
                .bytes(wasmBytes)
                .build();
        ModuleBytes payment = CasperDeployHelper.getPaymentModuleBytes(paymentAmount);

        return CasperDeployHelper.buildDeploy(signer, chainName, session, payment, gasPrice, ttl,
                date, dependencies);
    }
}
