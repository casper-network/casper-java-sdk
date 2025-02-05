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
        assertBytes(nativeTransactionTarget, "010000000000000000000100000000");
    }

    @Test
    void transactionSession() throws Exception {

        final String json = "{\"Session\":{\"is_install_upgrade\":true,\"runtime\":\"VmCasperV2\",\"module_bytes\":\"aab0da01340446cee477f28410f8af5d6e0f3a88fb26c0cafb8d1625f5cc9c10\",\"transferred_value\":0,\"seed\":null}}";
        final Session session = readJson(json, Session.class);

        assertThat(session.getRuntime().name(), is(TransactionRuntime.VM_CASPER_V2.name()));
        assertThat(session.getModuleBytes(), is(Hex.decode("aab0da01340446cee477f28410f8af5d6e0f3a88fb26c0cafb8d1625f5cc9c10")));
        assertJson(session, json);
        assertBytes(session, "060000000000000000000100010000000200020000000300110000000400310000000500390000003a0000000201010000000000000000000100000001aab0da01340446cee477f28410f8af5d6e0f3a88fb26c0cafb8d1625f5cc9c10000000000000000000");
    }

    @Test
    void transactionStoredByHash() throws Exception {

        final String json = "{\"Stored\":{\"id\":{\"ByHash\":\"37c80db9d769cb23ab482f44c2e8d8a73d9e24a1801e81d423953b8ba04b275d\"},\"runtime\":\"VmCasperV1\",\"transferred_value\":0}}";
        final Stored stored = readJson(json, Stored.class);

        assertThat(stored.getRuntime().name(), is(TransactionRuntime.VM_CASPER_V1.name()));
        assertThat(stored.getId(), is(instanceOf(ByHash.class)));
        assertThat(((ByHash) stored.getId()).getHashAddress(), is(new Digest("37c80db9d769cb23ab482f44c2e8d8a73d9e24a1801e81d423953b8ba04b275d")));
        assertJson(stored, json);
        assertBytes(stored, "03000000000000000000010001000000020036000000450000000102000000000000000000010001000000210000000037c80db9d769cb23ab482f44c2e8d8a73d9e24a1801e81d423953b8ba04b275d010000000000000000000100000000");
    }

    @Test
    void transactionStoredByName() throws Exception {

        final String json = "{\"Stored\":{\"id\":{\"ByName\":\"mint\"},\"runtime\":\"VmCasperV2\",\"transferred_value\":0}}";
        final Stored stored = readJson(json, Stored.class);

        assertThat(stored.getRuntime().name(), is(TransactionRuntime.VM_CASPER_V2.name()));
        assertThat(stored.getId(), is(instanceOf(ByName.class)));
        assertThat(((ByName) stored.getId()).getName(), is("mint"));
        assertJson(stored, json);
        assertBytes(stored, "0300000000000000000001000100000002001e0000002d00000001020000000000000000000100010000000900000001040000006d696e74010000000000000000000100000001");
    }


    @Test
    void transactionStoredByPackageHash() throws Exception {

        final String json = "{\"Stored\":{\"id\":{\"ByPackageHash\":{\"addr\":\"4b83ad1c5d842e5af75f5e77c56f11372217d8a5473a380fa9dfa96b8cd4c5e2\",\"version\":10}},\"runtime\":\"VmCasperV2\",\"transferred_value\":0}}";
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

        assertBytes(stored, "030000000000000000000100010000000200450000005400000001030000000000000000000100010000000200210000002a000000024b83ad1c5d842e5af75f5e77c56f11372217d8a5473a380fa9dfa96b8cd4c5e2010a00000000000000010000000000000000000100000001");
    }

    @Test
    void transactionStoredByPackageName() throws Exception {

        final String json = "{\"Stored\":{\"id\":{\"ByPackageName\":{\"name\":\"My Package\",\"version\":61523598}},\"runtime\":\"VmCasperV1\",\"transferred_value\":0}}";
        final Stored stored = readJson(json, Stored.class);

        assertThat(stored.getRuntime().name(), is(TransactionRuntime.VM_CASPER_V1.name()));
        assertThat(stored.getId(), is(instanceOf(ByPackageName.class)));
        assertThat(((ByPackageName) stored.getId()).getName(), is("My Package"));
        assertThat(((ByPackageName) stored.getId()).getVersion().isPresent(), is(true));
        assertThat(((ByPackageName) stored.getId()).getVersion().get(), is(61523598L));
        assertJson(stored, json);
        assertBytes(stored, "0300000000000000000001000100000002003300000042000000010300000000000000000001000100000002000f00000018000000030a0000004d79205061636b616765018ec6aa0300000000010000000000000000000100000000");
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
