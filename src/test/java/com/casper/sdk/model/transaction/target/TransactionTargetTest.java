package com.casper.sdk.model.transaction.target;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.common.Digest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syntifi.crypto.key.encdec.Hex;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;

/**
 * Unit tests for the {@link TransactionTarget} classes
 *
 * @author carl@stormeye.co.uk
 */
public class TransactionTargetTest {

    @Test
    void transactionNative() throws Exception {

        final String json = "\"Native\"";
        final Native nativeTransactionTarget = readJson(json, Native.class);

        assertThat(nativeTransactionTarget.getTarget(), is("Native"));
        assertJson(nativeTransactionTarget, json);
        assertBytes(nativeTransactionTarget, "00");
    }

    @Test
    void transactionSession() throws Exception {

        final String json = "{\"Session\":{\"is_install_upgrade\":true,\"runtime\":\"VmCasperV2\",\"module_bytes\":\"aab0da01340446cee477f28410f8af5d6e0f3a88fb26c0cafb8d1625f5cc9c10\",\"transferred_value\":0,\"seed\":null}}";
        final Session session = readJson(json, Session.class);

        assertThat(session.getRuntime().name(), is(TransactionRuntime.VM_CASPER_V2.name()));
        assertThat(session.getModuleBytes(), is(Hex.decode("aab0da01340446cee477f28410f8af5d6e0f3a88fb26c0cafb8d1625f5cc9c10")));
        assertJson(session, json);
        //assertBytes(session, "0220000000aab0da01340446cee477f28410f8af5d6e0f3a88fb26c0cafb8d1625f5cc9c1001");
        assertBytes(session, "0600000000000000000001000100000002000200000003000300000004002300000005002b0000002c000000020101aab0da01340446cee477f28410f8af5d6e0f3a88fb26c0cafb8d1625f5cc9c10000000000000000000");
    }

    @Test
    void transactionStoredByHash() throws Exception {

        final String json = "{\"Stored\":{\"id\":{\"ByHash\":\"37c80db9d769cb23ab482f44c2e8d8a73d9e24a1801e81d423953b8ba04b275d\"},\"runtime\":\"VmCasperV1\"}}";
        final Stored stored = readJson(json, Stored.class);

        assertThat(stored.getRuntime().name(), is(TransactionRuntime.VM_CASPER_V1.name()));
        assertThat(stored.getId(), is(instanceOf(ByHash.class)));
        assertThat(((ByHash) stored.getId()).getHashAddress(), is(new Digest("37c80db9d769cb23ab482f44c2e8d8a73d9e24a1801e81d423953b8ba04b275d")));
        assertJson(stored, json);
        assertBytes(stored, "010037c80db9d769cb23ab482f44c2e8d8a73d9e24a1801e81d423953b8ba04b275d00");

    }

    @Test
    void transactionStoredByName() throws Exception {

        final String json = "{\"Stored\":{\"id\":{\"ByName\":\"mint\"},\"runtime\":\"VmCasperV2\"}}";
        final Stored stored = readJson(json, Stored.class);

        assertThat(stored.getRuntime().name(), is(TransactionRuntime.VM_CASPER_V2.name()));
        assertThat(stored.getId(), is(instanceOf(ByName.class)));
        assertThat(((ByName) stored.getId()).getName(), is("mint"));
        assertJson(stored, json);
        assertBytes(stored, "0101040000006d696e7401");
    }


    @Test
    void transactionStoredByPackageHash() throws Exception {

        final String json = "{\"Stored\":{\"id\":{\"ByPackageHash\":{\"addr\":\"4b83ad1c5d842e5af75f5e77c56f11372217d8a5473a380fa9dfa96b8cd4c5e2\",\"version\":10}},\"runtime\":\"VmCasperV2\"}}";
        final Stored stored = readJson(json, Stored.class);

        assertThat(stored.getRuntime().name(), is(TransactionRuntime.VM_CASPER_V2.name()));
        assertThat(stored.getId(), is(instanceOf(ByPackageHash.class)));
        assertThat(
                ((ByPackageHash) stored.getId()).getAddr(),
                is(new Digest("4b83ad1c5d842e5af75f5e77c56f11372217d8a5473a380fa9dfa96b8cd4c5e2"))
        );
        assertThat(((ByPackageHash) stored.getId()).getVersion().isPresent(), is(true));
        assertThat(((ByPackageHash) stored.getId()).getVersion().get(), is(10L));

        assertJson(stored, json);

        assertBytes(stored, "01024b83ad1c5d842e5af75f5e77c56f11372217d8a5473a380fa9dfa96b8cd4c5e2010a00000001");
    }

    @Test
    void transactionStoredByPackageName() throws Exception {

        final String json = "{\"Stored\":{\"id\":{\"ByPackageName\":{\"name\":\"My Package\",\"version\":61523598}},\"runtime\":\"VmCasperV1\"}}";
        final Stored stored = readJson(json, Stored.class);

        assertThat(stored.getRuntime().name(), is(TransactionRuntime.VM_CASPER_V1.name()));
        assertThat(stored.getId(), is(instanceOf(ByPackageName.class)));
        assertThat(((ByPackageName) stored.getId()).getName(), is("My Package"));
        assertThat(((ByPackageName) stored.getId()).getVersion().isPresent(), is(true));
        assertThat(((ByPackageName) stored.getId()).getVersion().get(), is(61523598L));
        assertJson(stored, json);
        assertBytes(stored, "01030a0000004d79205061636b616765018ec6aa0300");
    }

    private static <T extends TransactionTarget> T readJson(final String json, final Class<T> expectedClass) throws JsonProcessingException {
        TransactionTarget target = new ObjectMapper().readValue(json, TransactionTarget.class);
        assertThat(target, is(instanceOf(expectedClass)));
        //noinspection unchecked
        return (T) target;
    }

    private static void assertJson(final TransactionTarget stored, final String expectedJson) throws JsonProcessingException {
        assertThat(new ObjectMapper().writeValueAsString(stored), is(expectedJson));
    }

    void assertBytes(final TransactionTarget transactionTarget, final String expectedHexBytes) throws NoSuchTypeException, ValueSerializationException {
        final SerializerBuffer serializerBuffer = new SerializerBuffer();
        transactionTarget.serialize(serializerBuffer, Target.BYTE);
        assertThat(Hex.encode(serializerBuffer.toByteArray()), is(expectedHexBytes));
    }
}
