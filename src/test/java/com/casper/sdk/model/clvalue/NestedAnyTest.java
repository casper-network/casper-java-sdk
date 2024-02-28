package com.casper.sdk.model.clvalue;

import com.casper.sdk.model.clvalue.cltype.CLTypeAny;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.deploy.NamedArg;
import com.casper.sdk.model.deploy.executabledeploy.ModuleBytes;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syntifi.crypto.key.encdec.Hex;
import dev.oak3.sbs4j.SerializerBuffer;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Test that a AbstractCLValueWithChildren provides a byte array string for undeserializable types (e.g. Any)
 *
 * @author ian@meywood.com
 */
public class NestedAnyTest {

    @Test
    void readStepEventWithMapContainingAnyValue() throws Exception {

        // This is a hex string that represents a map with a key of PublicKey and a value of Any
        final String hexBytes = "04000000010000000000000005000000010d23984fefcce099679a24496f1d3072a540b95d321f8ba951" +
                "df0cfe2c0691e5070280c6a47e8d030201000000013372bd275423a2191f3fc11e1bee0419fcb4890319873f455bb462e02b" +
                "199457070280c6a47e8d03011213e00a3bd748278b38a00a4787a7143f28a9d564126566716a53daa9499852070380c6a47e" +
                "8d03030100000001e45436d1609bd5c5ce13e8320a140421cf40a21089df1a08f7c58bcd06b6c9e4070380c6a47e8d030180" +
                "b99ded1d271c61a26d1b18c289ab33fc64355fa90cda4ae18f91786aa6ba4b070580c6a47e8d030501000000018973a4ffc1" +
                "7abab43f90bd229ef8a310236dd364ba0416d31612091d8cc17933070580c6a47e8d0301959d01aa68197e8cb91aa06bcc92" +
                "0f8d4a245dff60ea726bb89255349107a565070180c6a47e8d03010100000001a1cce6ea8db0d11d95b83671cc69f726b36a" +
                "afb46dbf808c608dbcd5d237b11c070180c6a47e8d0301fcf1392c59c7d89190bfcd1b00902cc0801700eab98034aa4e5681" +
                "6d338f6c25070480c6a47e8d030401000000016997c57f973cd02df90941b7ace79d2b49e173f5f14a343cbf69a7a7ff8be3" +
                "b5070480c6a47e8d03020000000000000005000000010d23984fefcce099679a24496f1d3072a540b95d321f8ba951df0cfe" +
                "2c0691e5070280c6a47e8d030201000000013372bd275423a2191f3fc11e1bee0419fcb4890319873f455bb462e02b199457" +
                "070280c6a47e8d03011213e00a3bd748278b38a00a4787a7143f28a9d564126566716a53daa9499852070380c6a47e8d0303" +
                "0100000001e45436d1609bd5c5ce13e8320a140421cf40a21089df1a08f7c58bcd06b6c9e4070380c6a47e8d030180b99ded" +
                "1d271c61a26d1b18c289ab33fc64355fa90cda4ae18f91786aa6ba4b070580c6a47e8d030501000000018973a4ffc17abab4" +
                "3f90bd229ef8a310236dd364ba0416d31612091d8cc17933070580c6a47e8d0301959d01aa68197e8cb91aa06bcc920f8d4a" +
                "245dff60ea726bb89255349107a565070180c6a47e8d03010100000001a1cce6ea8db0d11d95b83671cc69f726b36aafb46d" +
                "bf808c608dbcd5d237b11c070180c6a47e8d0301fcf1392c59c7d89190bfcd1b00902cc0801700eab98034aa4e56816d338f" +
                "6c25070480c6a47e8d030401000000016997c57f973cd02df90941b7ace79d2b49e173f5f14a343cbf69a7a7ff8be3b50704" +
                "80c6a47e8d03030000000000000005000000010d23984fefcce099679a24496f1d3072a540b95d321f8ba951df0cfe2c0691" +
                "e5070280c6a47e8d030201000000013372bd275423a2191f3fc11e1bee0419fcb4890319873f455bb462e02b199457070280" +
                "c6a47e8d03011213e00a3bd748278b38a00a4787a7143f28a9d564126566716a53daa9499852070380c6a47e8d0303010000" +
                "0001e45436d1609bd5c5ce13e8320a140421cf40a21089df1a08f7c58bcd06b6c9e4070380c6a47e8d030180b99ded1d271c" +
                "61a26d1b18c289ab33fc64355fa90cda4ae18f91786aa6ba4b070580c6a47e8d030501000000018973a4ffc17abab43f90bd" +
                "229ef8a310236dd364ba0416d31612091d8cc17933070580c6a47e8d0301959d01aa68197e8cb91aa06bcc920f8d4a245dff" +
                "60ea726bb89255349107a565070180c6a47e8d03010100000001a1cce6ea8db0d11d95b83671cc69f726b36aafb46dbf808c" +
                "608dbcd5d237b11c070180c6a47e8d0301fcf1392c59c7d89190bfcd1b00902cc0801700eab98034aa4e56816d338f6c2507" +
                "0480c6a47e8d030401000000016997c57f973cd02df90941b7ace79d2b49e173f5f14a343cbf69a7a7ff8be3b5070480c6a4" +
                "7e8d03040000000000000005000000010d23984fefcce099679a24496f1d3072a540b95d321f8ba951df0cfe2c0691e50702" +
                "80c6a47e8d030201000000013372bd275423a2191f3fc11e1bee0419fcb4890319873f455bb462e02b199457070280c6a47e" +
                "8d03011213e00a3bd748278b38a00a4787a7143f28a9d564126566716a53daa9499852070380c6a47e8d03030100000001e4" +
                "5436d1609bd5c5ce13e8320a140421cf40a21089df1a08f7c58bcd06b6c9e4070380c6a47e8d030180b99ded1d271c61a26d" +
                "1b18c289ab33fc64355fa90cda4ae18f91786aa6ba4b070580c6a47e8d030501000000018973a4ffc17abab43f90bd229ef8" +
                "a310236dd364ba0416d31612091d8cc17933070580c6a47e8d0301959d01aa68197e8cb91aa06bcc920f8d4a245dff60ea72" +
                "6bb89255349107a565070180c6a47e8d03010100000001a1cce6ea8db0d11d95b83671cc69f726b36aafb46dbf808c608dbc" +
                "d5d237b11c070180c6a47e8d0301fcf1392c59c7d89190bfcd1b00902cc0801700eab98034aa4e56816d338f6c25070480c6" +
                "a47e8d030401000000016997c57f973cd02df90941b7ace79d2b49e173f5f14a343cbf69a7a7ff8be3b5070480c6a47e8d03";

        final String json = " {\n" +
                "  \"cl_type\": {\n" +
                "    \"Map\": {\n" +
                "      \"key\": \"PublicKey\",\n" +
                "      \"value\": \"Any\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"bytes\": \"" + hexBytes + "\",\n" +
                "  \"parsed\": null\n" +
                "}";

        final CLValueMap clValueMap = (CLValueMap) new ObjectMapper().readValue(json, AbstractCLValue.class);

        assertThat(clValueMap, is(notNullValue()));
        assertThat(clValueMap.getClType().isDeserializable(), is(false));
        assertThat(clValueMap.getBytes(), is(hexBytes));
    }

    @Test
    public void nestedEmptyMapiWithoutAny() throws Exception {

        final String json = " {\n" +
                "  \"cl_type\": {\n" +
                "    \"Map\": {\n" +
                "      \"key\": \"PublicKey\",\n" +
                "      \"value\": \"U256\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"bytes\": \"00000000\",\n" +
                "  \"parsed\": null\n" +
                "}";

        final CLValueMap clValueMap = (CLValueMap) new ObjectMapper().readValue(json, AbstractCLValue.class);
        assertThat(clValueMap, is(notNullValue()));
        assertThat(clValueMap.getClType().isDeserializable(), is(true));
        assertThat(clValueMap.getBytes(), is("00000000"));
    }

    @Test
    void nestedListWithAny() throws Exception {

        final String json = " {\n" +
                "  \"cl_type\": {\n" +
                "    \"List\": \"Any\"\n" +
                "  },\n" +
                "  \"bytes\": \"00000000\",\n" +
                "  \"parsed\": null\n" +
                "}";

        final CLValueList clValueList = (CLValueList) new ObjectMapper().readValue(json, AbstractCLValue.class);
        assertThat(clValueList, is(notNullValue()));
        assertThat(clValueList.getClType().isDeserializable(), is(false));
        assertThat(clValueList.getBytes(), is("00000000"));
    }

    @Test
    void nestedListWithoutAny() throws Exception {

        final String json = " {\n" +
                "  \"cl_type\": {\n" +
                "    \"List\": \"U256\"\n" +
                "  },\n" +
                "  \"bytes\": \"00000000\",\n" +
                "  \"parsed\": null\n" +
                "}";

        final CLValueList clValueList = (CLValueList) new ObjectMapper().readValue(json, AbstractCLValue.class);
        assertThat(clValueList, is(notNullValue()));
        assertThat(clValueList.getClType().isDeserializable(), is(true));
        assertThat(clValueList.getBytes(), is("00000000"));
    }


    @Test
    void anyNamedArgument() throws Exception {

        final CLValueAny clValueAny = new CLValueAny(Hex.decode("d2029649"));
        final NamedArg<CLTypeAny> namedArg = new NamedArg<>("any", clValueAny);

        final ModuleBytes moduleBytes = new ModuleBytes();
        moduleBytes.setJsonBytes("01020304");
        moduleBytes.setArgs(Collections.singletonList(namedArg));

        final SerializerBuffer ser = new SerializerBuffer();
        moduleBytes.serialize(ser, Target.BYTE);

        byte[] actual = ser.toByteArray();
        byte[] expected = Hex.decode("0004000000010203040100000003000000616e7904000000d202964915");
        assertThat(actual, is(expected));


    }
}
