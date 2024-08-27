package com.casper.sdk.model.transaction.transactiontarget;

import com.casper.sdk.model.transaction.target.Native;
import com.casper.sdk.model.transaction.target.Session;
import com.casper.sdk.model.transaction.target.TransactionRuntime;
import com.casper.sdk.model.transaction.target.TransactionTarget;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syntifi.crypto.key.encdec.Hex;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;

/**
 * @author carl@stormeye.co.uk
 */
public class TransactionTargetTest {

    @Test
    void transactionNative() throws JsonProcessingException {

        final String json = "\"Native\"";

        final TransactionTarget nativeTransactionTarget = new ObjectMapper().readValue(json, TransactionTarget.class);

        assertThat(nativeTransactionTarget, is(instanceOf(Native.class)));

        final Native nativeType = (Native) nativeTransactionTarget;

        assertThat(nativeType.getTarget(), is("Native"));

        assertThat(new ObjectMapper().writeValueAsString(nativeTransactionTarget), is(json));
    }

    @Test
    void transactionSession() throws JsonProcessingException {

        final String json = "{\"Session\":{\"module_bytes\":\"aab0da01340446cee477f28410f8af5d6e0f3a88fb26c0cafb8d1625f5cc9c10\",\"runtime\":\"VmCasperV2\"}}";

        final TransactionTarget sessionTransactionTarget = new ObjectMapper().readValue(json, TransactionTarget.class);

        assertThat(sessionTransactionTarget, is(instanceOf(Session.class)));
        final Session session = (Session) sessionTransactionTarget;

        assertThat(session.getRuntime().name(), is(TransactionRuntime.VM_CASPER_V2.name()));
        assertThat(session.getModuleBytes(), is(Hex.decode("aab0da01340446cee477f28410f8af5d6e0f3a88fb26c0cafb8d1625f5cc9c10")));

        assertThat(new ObjectMapper().writeValueAsString(session), is(json));

    }

    @Test
    void transactionStored() {}

}
