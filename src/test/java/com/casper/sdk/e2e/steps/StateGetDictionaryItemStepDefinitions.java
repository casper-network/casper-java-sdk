package com.casper.sdk.e2e.steps;

import com.casper.sdk.e2e.utils.AssetUtils;
import com.casper.sdk.e2e.utils.CasperClientProvider;
import com.casper.sdk.identifier.block.BlockIdentifier;
import com.casper.sdk.identifier.block.HashBlockIdentifier;
import com.casper.sdk.identifier.dictionary.StringDictionaryIdentifier;
import com.casper.sdk.model.account.AccountData;
import com.casper.sdk.model.block.JsonBlockData;
import com.casper.sdk.model.dictionary.DictionaryData;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.stateroothash.StateRootHashData;
import com.casper.sdk.model.storedvalue.StoredValueAccount;
import com.casper.sdk.service.CasperService;
import com.casper.sdk.e2e.utils.ContextMap;
import com.syntifi.crypto.key.Ed25519PrivateKey;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Step definitions for the state_get_dictionary_item_result RCP method call.
 *
 * @author ian@meywood.com
 */
public class StateGetDictionaryItemStepDefinitions {

    private final ContextMap contextMap = ContextMap.getInstance();
    private final Logger logger = LoggerFactory.getLogger(StateGetDictionaryItemStepDefinitions.class);
    public final CasperService casperService = CasperClientProvider.getInstance().getCasperService();

    @Given("that the state_get_dictionary_item RCP method is invoked")
    public void thatTheState_get_dictionary_itemRCPMethodIsInvoked() throws IOException {
        logger.info("Given that the state_get_dictionary_item RCP method is invoked");

        final StateRootHashData stateRootHash = casperService.getStateRootHash();

        final URL faucetPrivateKeyUrl = AssetUtils.getFaucetAsset(1, "secret_key.pem");
        final Ed25519PrivateKey privateKey = new Ed25519PrivateKey();
        privateKey.readPrivateKey(faucetPrivateKeyUrl.getFile());
        final PublicKey publicKey = PublicKey.fromAbstractPublicKey(privateKey.derivePublicKey());

        final JsonBlockData block = CasperClientProvider.getInstance().getCasperService().getBlock();
        final BlockIdentifier identifier = new HashBlockIdentifier(block.getBlock().getHash().toString());

        final AccountData accountData = casperService.getStateAccountInfo(publicKey.getAlgoTaggedHex(), identifier);
        this.contextMap.put("mainPurse", accountData.getAccount().getMainPurse());

        final String accountHash = publicKey.generateAccountHash(true);
        final StringDictionaryIdentifier key = StringDictionaryIdentifier.builder().dictionary(accountHash).build();
        this.contextMap.put("accountHash", accountHash);

        final DictionaryData dictionaryData = casperService.getStateDictionaryItem(
                stateRootHash.getStateRootHash(),
                key
        );

        contextMap.put(StepConstants.STATE_GET_DICTIONARY_ITEM, dictionaryData);
    }

    @Then("a valid state_get_dictionary_item_result is returned")
    public void aValidState_get_dictionary_item_resultIsReturned() {
        logger.info("Then a valid state_get_dictionary_item_result is returned");
        final DictionaryData dictionaryData = contextMap.get(StepConstants.STATE_GET_DICTIONARY_ITEM);
        assertThat(dictionaryData, is(notNullValue()));

        final String accountHash = this.contextMap.get("accountHash");
        assertThat(dictionaryData.getDictionaryKey(), is(accountHash));
        assertThat(dictionaryData.getStoredValue(), is(instanceOf(StoredValueAccount.class)));

        final StoredValueAccount storedValueAccount = (StoredValueAccount) dictionaryData.getStoredValue();
        assertThat(storedValueAccount.getValue().getHash(), is(accountHash));
        final String mainPurse = this.contextMap.get("mainPurse");
        assertThat(storedValueAccount.getValue().getMainPurse(), is(mainPurse));
    }
}
