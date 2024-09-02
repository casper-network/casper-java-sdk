package com.casper.sdk.model.transaction.target;

import com.casper.sdk.model.common.Digest;
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
    void transactionStoredByHash() throws Exception {

        final String json = "{\"Stored\":{\"id\":{\"ByHash\":\"37c80db9d769cb23ab482f44c2e8d8a73d9e24a1801e81d423953b8ba04b275d\"},\"runtime\":\"VmCasperV1\"}}";
        final TransactionTarget stored = new ObjectMapper().readValue(json, TransactionTarget.class);
        assertThat(stored, is(instanceOf(Stored.class)));

        assertThat(((Stored) stored).getRuntime().name(), is(TransactionRuntime.VM_CASPER_V1.name()));
        assertThat(((Stored) stored).getId(), is(instanceOf(ByHash.class)));
        assertThat(((ByHash) ((Stored) stored).getId()).getHashAddress(), is(new Digest("37c80db9d769cb23ab482f44c2e8d8a73d9e24a1801e81d423953b8ba04b275d")));
        assertThat(new ObjectMapper().writeValueAsString(stored), is(json));
    }

    @Test
    void transactionStoredByName() throws Exception {

        final String json = "{\"Stored\":{\"id\":{\"ByName\":\"mint\"},\"runtime\":\"VmCasperV1\"}}";
        final TransactionTarget stored = new ObjectMapper().readValue(json, TransactionTarget.class);
        assertThat(stored, is(instanceOf(Stored.class)));

        assertThat(((Stored) stored).getRuntime().name(), is(TransactionRuntime.VM_CASPER_V1.name()));
        assertThat(((Stored) stored).getId(), is(instanceOf(ByName.class)));
        assertThat(((ByName) ((Stored) stored).getId()).getName(), is("mint"));
        assertThat(new ObjectMapper().writeValueAsString(stored), is(json));
    }

    @Test
    void transactionStoredByPackageHash() throws Exception {

        final String json = "{\"Stored\":{\"id\":{\"ByPackageHash\":{\"addr\":\"4b83ad1c5d842e5af75f5e77c56f11372217d8a5473a380fa9dfa96b8cd4c5e2\",\"version\":10}},\"runtime\":\"VmCasperV1\"}}";

        final TransactionTarget stored = new ObjectMapper().readValue(json, TransactionTarget.class);
        assertThat(stored, is(instanceOf(Stored.class)));

        assertThat(((Stored) stored).getRuntime().name(), is(TransactionRuntime.VM_CASPER_V1.name()));
        assertThat(((Stored) stored).getId(), is(instanceOf(ByPackageHash.class)));
        assertThat(
                ((ByPackageHash) ((Stored) stored).getId()).getAddr(),
                is(new Digest("4b83ad1c5d842e5af75f5e77c56f11372217d8a5473a380fa9dfa96b8cd4c5e2"))
        );
        assertThat(((ByPackageHash) ((Stored) stored).getId()).getVersion().isPresent(), is(true));
        assertThat(((ByPackageHash) ((Stored) stored).getId()).getVersion().get(), is(10L));
        assertThat(new ObjectMapper().writeValueAsString(stored), is(json));
    }

    @Test
    void transactionStoredByPackageName() throws Exception {

        final String json = "{\"Stored\":{\"id\":{\"ByPackageName\":{\"name\":\"My Package\",\"version\":61523598}},\"runtime\":\"VmCasperV1\"}}";
        final TransactionTarget stored = new ObjectMapper().readValue(json, TransactionTarget.class);
        assertThat(stored, is(instanceOf(Stored.class)));

        assertThat(((Stored) stored).getRuntime().name(), is(TransactionRuntime.VM_CASPER_V1.name()));
        assertThat(((Stored) stored).getId(), is(instanceOf(ByPackageName.class)));
        assertThat(((ByPackageName) ((Stored) stored).getId()).getName(), is("My Package"));
        assertThat(((ByPackageName) ((Stored) stored).getId()).getVersion().isPresent(), is(true));
        assertThat(((ByPackageName) ((Stored) stored).getId()).getVersion().get(), is(61523598L));
        assertThat(new ObjectMapper().writeValueAsString(stored), is(json));
    }
}
