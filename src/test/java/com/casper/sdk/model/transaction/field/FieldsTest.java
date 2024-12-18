package com.casper.sdk.model.transaction.field;

import com.casper.sdk.model.AbstractJsonTests;
import com.casper.sdk.model.clvalue.CLValuePublicKey;
import com.casper.sdk.model.clvalue.CLValueString;
import com.casper.sdk.model.clvalue.CLValueU512;
import com.casper.sdk.model.clvalue.CLValueU8;
import com.casper.sdk.model.deploy.NamedArg;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.transaction.NamedArgs;
import com.casper.sdk.model.transaction.entrypoint.CallEntryPoint;
import com.casper.sdk.model.transaction.scheduling.Standard;
import com.casper.sdk.model.transaction.target.Session;
import com.casper.sdk.model.transaction.target.TransactionRuntime;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author ian@meywood.com
 */
public class FieldsTest extends AbstractJsonTests {

    @Test
    void argsFieldsJsonRoundTrip() throws Exception {

        final String json = IOUtils.toString(
                Objects.requireNonNull(getClass().getResource("/transaction-samples/args_fields.json")).openStream(),
                StandardCharsets.UTF_8
        );

        final CLValuePublicKey target = new CLValuePublicKey(PublicKey.fromTaggedHexString("0106ed45915392c02b37136618372ac8dde8e0e3b8ee6190b2ca6db539b354ede4"));
        final CLValueU512 amount = new CLValueU512(new BigInteger("2500000000"));

        final NamedArgs args = new NamedArgs(
                new NamedArg<>("target", target),
                new NamedArg<>("amount", amount)
        );

        final Fields fields = new Fields();
        fields.setArgs(args);

        final String writtenJson = new ObjectMapper().writeValueAsString(fields);

        JSONAssert.assertEquals(json, writtenJson, false);

        final Fields fromJson = new ObjectMapper().readValue(json, Fields.class);

        assertThat(fromJson.getArgs(), is(notNullValue()));
        assertThat(fromJson.getArgs().getArgs().get(0).getType(), is("target"));
        assertThat(fromJson.getArgs().getArgs().get(0).getClValue(), is(target));
        assertThat(fromJson.getArgs().getArgs().get(1).getType(), is("amount"));
        assertThat(fromJson.getArgs().getArgs().get(1).getClValue(), is(amount));
    }

    @Test
    void fieldsJsonRoundTrip() throws Exception {

        final String json = IOUtils.toString(
                Objects.requireNonNull(getClass().getResource("/transaction-samples/fields.json")).openStream(),
                StandardCharsets.UTF_8
        );

        final Fields fromJson = new ObjectMapper().readValue(json, Fields.class);

        assertThat(fromJson.getArgs(), is(Matchers.notNullValue()));
        assertThat(fromJson.getArgs().getArgs().get(0).getType(), is("name"));
        assertThat(fromJson.getArgs().getArgs().get(0).getClValue(), is(new CLValueString("Test")));
        assertThat(fromJson.getArgs().getArgs().get(1).getType(), is("symbol"));
        assertThat(fromJson.getArgs().getArgs().get(1).getClValue(), is(new CLValueString("test")));
        assertThat(fromJson.getArgs().getArgs().get(2).getType(), is("decimals"));
        assertThat(fromJson.getArgs().getArgs().get(2).getClValue(), is(new CLValueU8((byte) 9)));
        assertThat(fromJson.getArgs().get("enable_mint_burn").getClValue(), is(new CLValueU8((byte) 1)));
        assertThat(fromJson.getEntryPoint(), is(instanceOf(CallEntryPoint.class)));
        assertThat(fromJson.getScheduling(), is(instanceOf(Standard.class)));
        assertThat(fromJson.getTarget(), is(instanceOf(Session.class)));

        final Session session = (Session) fromJson.getTarget();
        assertThat(session.isInstallUpgrade(), is(true));
        assertThat(session.getRuntime(), is(TransactionRuntime.VM_CASPER_V1));
        assertThat(session.getTransferredValue(), is(0L));
        assertThat(session.getSeed(), is(nullValue()));

        final String writtenJson = getPrettyJson(fromJson);
        JSONAssert.assertEquals(json, writtenJson, false);
    }
}
