package com.howto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

import com.casper.sdk.model.clvalue.CLValuePublicKey;
import com.casper.sdk.model.clvalue.CLValueU512;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.deploy.NamedArg;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.transaction.GetTransactionResult;
import com.casper.sdk.model.transaction.InitiatorPublicKey;
import com.casper.sdk.model.transaction.NamedArgs;
import com.casper.sdk.model.transaction.PutTransactionResult;
import com.casper.sdk.model.transaction.TransactionV1;
import com.casper.sdk.model.transaction.TransactionV1Payload;
import com.casper.sdk.model.transaction.entrypoint.DelegateEntryPoint;
import com.casper.sdk.model.transaction.execution.ExecutionResultV2;
import com.casper.sdk.model.transaction.field.Fields;
import com.casper.sdk.model.transaction.pricing.PaymentLimited;
import com.casper.sdk.model.transaction.scheduling.Standard;
import com.casper.sdk.model.transaction.target.Native;
import com.casper.sdk.model.transaction.target.Transaction;
import com.casper.sdk.service.CasperService;
import com.syntifi.crypto.key.Ed25519PrivateKey;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HowToDelegate extends HowToMethods {

    private CasperService casperService;

    @BeforeEach
    public void init() throws IOException, URISyntaxException {
        casperService = connect();
    }

    @Test
    void delegate()
        throws ValueSerializationException, TimeoutException, NoSuchAlgorithmException, IOException, URISyntaxException {

        final PublicKey delegator = PublicKey.fromTaggedHexString("013f69f01439509c0fd113176bcaec9170d18b70fb2bbbdcc9b28803f8bbcc429c");
        final PublicKey validator = PublicKey.fromTaggedHexString("0153d98c835b493c76050735dc79e6702a17cd78ab69d5b0c3631e72f8f38bb095");

        final String secretKeyPath = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("howto/keys/delegator.pem")).toURI()).toString();
        final Ed25519PrivateKey delegatorPrivateKey = new Ed25519PrivateKey();
        delegatorPrivateKey.readPrivateKey(secretKeyPath);

        final List<NamedArg<?>> args = Arrays.asList(
            new NamedArg<>("amount", new CLValueU512(new BigInteger("500000000000"))),
            new NamedArg<>("delegator", new CLValuePublicKey(PublicKey.fromAbstractPublicKey(delegator.getPubKey()))),
            new NamedArg<>("validator", new CLValuePublicKey(PublicKey.fromAbstractPublicKey(validator.getPubKey())))
        );
        final TransactionV1Payload payload = TransactionV1Payload.builder()
            .chainName(casperService.getStatus().getChainSpecName())
            .ttl(Ttl.builder().ttl("30m").build())
            .pricingMode(new PaymentLimited(1, new BigInteger("2500000000"), true))
            .initiatorAddr(new InitiatorPublicKey(PublicKey.fromAbstractPublicKey(delegator.getPubKey())))
            .fields(Fields.builder()
                .args(new NamedArgs(args))
                .scheduling(new Standard())
                .target(new Native())
                .entryPoint(new DelegateEntryPoint()).build()
            )
            .build();

        final TransactionV1 transactionV1 = TransactionV1.builder()
            .payload(payload)
            .build();

        final Transaction transaction = new Transaction(transactionV1.sign(delegatorPrivateKey));

        final PutTransactionResult result = casperService.putTransaction(transaction);

        assert result != null;
        assert result.getTransactionHash() != null;

        final GetTransactionResult transactionResult = waitForTransaction(result.getTransactionHash());

        assertThat(transactionResult, is(notNullValue()));
        assertThat(((ExecutionResultV2) transactionResult.getExecutionInfo().getExecutionResult()).getErrorMessage(), is(nullValue()));

    }

    /**
     * {
     *   "json": {
     *     "api_version": "2.0.0",
     *     "transaction": {
     *       "Version1": {
     *         "hash": "cd7d70f7e74225c69d434f7847394ae87d8bae81699115842db270d01260d819",
     *         "payload": {
     *           "ttl": "30m",
     *           "fields": {
     *             "args": {
     *               "Named": [
     *                 [
     *                   "delegator",
     *                   {
     *                     "bytes": "020257bb15beb8d49295984007799e09692ba1779b3194610e21feb96d5c5b774f9c",
     *                     "parsed": "020257bb15beb8d49295984007799e09692ba1779b3194610e21feb96d5c5b774f9c",
     *                     "cl_type": "PublicKey"
     *                   }
     *                 ],
     *                 [
     *                   "validator",
     *                   {
     *                     "bytes": "0153d98c835b493c76050735dc79e6702a17cd78ab69d5b0c3631e72f8f38bb095",
     *                     "parsed": "0153d98c835b493c76050735dc79e6702a17cd78ab69d5b0c3631e72f8f38bb095",
     *                     "cl_type": "PublicKey"
     *                   }
     *                 ],
     *                 [
     *                   "amount",
     *                   {
     *                     "bytes": "050088526a74",
     *                     "parsed": "500000000000",
     *                     "cl_type": "U512"
     *                   }
     *                 ]
     *               ]
     *             },
     *             "target": "Native",
     *             "scheduling": "Standard",
     *             "entry_point": "Delegate"
     *           },
     *           "timestamp": "2025-10-04T13:31:17.978Z",
     *           "chain_name": "casper-test",
     *           "pricing_mode": {
     *             "PaymentLimited": {
     *               "payment_amount": 2500000000,
     *               "standard_payment": true,
     *               "gas_price_tolerance": 1
     *             }
     *           },
     *           "initiator_addr": {
     *             "PublicKey": "020257bb15beb8d49295984007799e09692ba1779b3194610e21feb96d5c5b774f9c"
     *           }
     *         },
     *         "approvals": [
     *           {
     *             "signer": "020257bb15beb8d49295984007799e09692ba1779b3194610e21feb96d5c5b774f9c",
     *             "signature": "0217f72d98caa781eab18c237ca5141043b2c28fab8becbc1d7118bc61c7a9aa4b49ab018d78decf44e2409d4de6a5a931c0653f4e503b00218817bc033e8aef0d"
     *           }
     *         ]
     *       }
     *     },
     *     "execution_info": {
     *       "block_hash": "3a67c07282a5669f16a5fc2963131c39260ab57aca2d81fd6b8ba1ee0a0e5e9a",
     *       "block_height": 5705707,
     *       "execution_result": {
     *         "Version2": {
     *           "cost": "2500000000",
     *           "limit": "2500000000",
     *           "refund": "0",
     *           "effects": [
     *             {
     *               "key": "balance-hold-014323a32eb6ee5cd7bb718a16c115ae4bb5cadcc0b4249cc5e7e1cef3b03fd6d9310b6caf99010000",
     *               "kind": {
     *                 "Write": {
     *                   "CLValue": {
     *                     "bytes": "0400f90295",
     *                     "parsed": "2500000000",
     *                     "cl_type": "U512"
     *                   }
     *                 }
     *               }
     *             },
     *             {
     *               "key": "bid-addr-01f449577227a5934682fb0e7b00cee310416c2f3e1ce84903999d55c816ecdd58",
     *               "kind": "Identity"
     *             },
     *             {
     *               "key": "uref-474f7030096735ecc6646234cc4a0366c259eb07f8a3dd6f69fb1000360c5960-000",
     *               "kind": {
     *                 "Write": {
     *                   "CLValue": {
     *                     "bytes": "",
     *                     "parsed": null,
     *                     "cl_type": "Unit"
     *                   }
     *                 }
     *               }
     *             },
     *             {
     *               "key": "balance-474f7030096735ecc6646234cc4a0366c259eb07f8a3dd6f69fb1000360c5960",
     *               "kind": {
     *                 "Write": {
     *                   "CLValue": {
     *                     "bytes": "00",
     *                     "parsed": "0",
     *                     "cl_type": "U512"
     *                   }
     *                 }
     *               }
     *             },
     *             {
     *               "key": "balance-hold-014323a32eb6ee5cd7bb718a16c115ae4bb5cadcc0b4249cc5e7e1cef3b03fd6d9310b6caf99010000",
     *               "kind": "Identity"
     *             },
     *             {
     *               "key": "balance-4323a32eb6ee5cd7bb718a16c115ae4bb5cadcc0b4249cc5e7e1cef3b03fd6d9",
     *               "kind": {
     *                 "Write": {
     *                   "CLValue": {
     *                     "bytes": "050088526a74",
     *                     "parsed": "500000000000",
     *                     "cl_type": "U512"
     *                   }
     *                 }
     *               }
     *             },
     *             {
     *               "key": "balance-474f7030096735ecc6646234cc4a0366c259eb07f8a3dd6f69fb1000360c5960",
     *               "kind": {
     *                 "AddUInt512": "500000000000"
     *               }
     *             },
     *             {
     *               "key": "bid-addr-02f449577227a5934682fb0e7b00cee310416c2f3e1ce84903999d55c816ecdd58db5f8d1611a01752026e0aa62c24e05d32d837e27bea83d2de09bf4c981a5f08",
     *               "kind": {
     *                 "Write": {
     *                   "BidKind": {
     *                     "Delegator": {
     *                       "bonding_purse": "uref-474f7030096735ecc6646234cc4a0366c259eb07f8a3dd6f69fb1000360c5960-007",
     *                       "staked_amount": "500000000000",
     *                       "delegator_kind": {
     *                         "PublicKey": "020257bb15beb8d49295984007799e09692ba1779b3194610e21feb96d5c5b774f9c"
     *                       },
     *                       "vesting_schedule": null,
     *                       "validator_public_key": "0153d98c835b493c76050735dc79e6702a17cd78ab69d5b0c3631e72f8f38bb095"
     *                     }
     *                   }
     *                 }
     *               }
     *             },
     *             {
     *               "key": "balance-hold-014323a32eb6ee5cd7bb718a16c115ae4bb5cadcc0b4249cc5e7e1cef3b03fd6d9310b6caf99010000",
     *               "kind": {
     *                 "Prune": "balance-hold-014323a32eb6ee5cd7bb718a16c115ae4bb5cadcc0b4249cc5e7e1cef3b03fd6d9310b6caf99010000"
     *               }
     *             },
     *             {
     *               "key": "balance-4323a32eb6ee5cd7bb718a16c115ae4bb5cadcc0b4249cc5e7e1cef3b03fd6d9",
     *               "kind": {
     *                 "Write": {
     *                   "CLValue": {
     *                     "bytes": "05008f4fd573",
     *                     "parsed": "497500000000",
     *                     "cl_type": "U512"
     *                   }
     *                 }
     *               }
     *             },
     *             {
     *               "key": "balance-8523a88db52d10a9387f86dae4802747883195c99e8fc9583da88004cde49fef",
     *               "kind": {
     *                 "AddUInt512": "2500000000"
     *               }
     *             }
     *           ],
     *           "consumed": "2500000000",
     *           "initiator": {
     *             "PublicKey": "020257bb15beb8d49295984007799e09692ba1779b3194610e21feb96d5c5b774f9c"
     *           },
     *           "transfers": [
     *             {
     *               "Version2": {
     *                 "id": null,
     *                 "to": "account-hash-6174cf2e6f8fed1715c9a3bace9c50bfe572eecb763b0ed3f644532616452008",
     *                 "gas": "100000000",
     *                 "from": {
     *                   "AccountHash": "account-hash-db5f8d1611a01752026e0aa62c24e05d32d837e27bea83d2de09bf4c981a5f08"
     *                 },
     *                 "amount": "500000000000",
     *                 "source": "uref-4323a32eb6ee5cd7bb718a16c115ae4bb5cadcc0b4249cc5e7e1cef3b03fd6d9-007",
     *                 "target": "uref-474f7030096735ecc6646234cc4a0366c259eb07f8a3dd6f69fb1000360c5960-007",
     *                 "transaction_hash": {
     *                   "Version1": "cd7d70f7e74225c69d434f7847394ae87d8bae81699115842db270d01260d819"
     *                 }
     *               }
     *             }
     *           ],
     *           "current_price": 1,
     *           "error_message": null,
     *           "size_estimate": 536
     *         }
     *       }
     *     }
     *   }
     * }
     *
     *
     *
     */



}
