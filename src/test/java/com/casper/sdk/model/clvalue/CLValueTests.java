package com.casper.sdk.model.clvalue;

import com.casper.sdk.exception.DynamicInstanceException;
import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.cltype.CLTypeAny;
import com.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.casper.sdk.model.clvalue.serde.Target;
import com.syntifi.crypto.key.encdec.Hex;
import dev.oak3.sbs4j.SerializerBuffer;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;

public class CLValueTests {

    @Test
    void allCLTypes_must_be_implemented() throws DynamicInstanceException, NoSuchTypeException {
        for (CLTypeData clTypeData : CLTypeData.values()) {
            // Ensure there are no types with missing implementation
            assertNotNull(clTypeData.getClazz());

            AbstractCLValue<?, ?> clValue = CLTypeData.createCLValueFromCLTypeData(clTypeData);

            // Correct instance type
            assertEquals(clTypeData.getClazz(), clValue.getClass());

            // Check if the correct CLType is set
            assertEquals(clTypeData, clValue.getClType().getClTypeData());
        }
    }

    @Test
    void getTypeBySerializationTag_should_return_correct_CLTypeData() throws NoSuchTypeException {
        for (CLTypeData typeData : CLTypeData.values()) {
            assertEquals(typeData, CLTypeData.getTypeBySerializationTag(typeData.getSerializationTag()));
        }
    }

    @Test
    void createCLTypeFromCLTypeData_should_return_correct_CLType() throws DynamicInstanceException {
        assertTrue(CLTypeData.createCLTypeFromCLTypeData(CLTypeData.ANY) instanceof CLTypeAny);
    }

    @Test
    void createCLValueFromCLTypeName_should_return_correct_CLValue() throws DynamicInstanceException, NoSuchTypeException {
        assertTrue(CLTypeData.createCLValueFromCLTypeName(CLTypeData.ANY.getClTypeName()) instanceof CLValueAny);
    }

    @Test
    void createCLValueFromCLTypeName_from_CLTypeData_should_throw_NoSuchTypeException() {
        assertThrows(NoSuchTypeException.class, () -> CLTypeData.createCLValueFromCLTypeName("NE"));
    }

    @Test
    void getClassByName_from_CLTypeData_should_throw_NoSuchTypeException() {
        assertThrows(NoSuchTypeException.class, () -> CLTypeData.getClassByName("NE"));
    }

    @Test
    void getCLTypeClassByName_from_CLTypeData_should_throw_NoSuchTypeException() {
        assertThrows(NoSuchTypeException.class, () -> CLTypeData.getCLTypeClassByName("NE"));
    }

    @Test
    void clValueAnyByteSerializationWithAndWithoutTypeInfo() throws Exception {

        final CLValueAny clValueAny = new CLValueAny(Hex.decode("d2029649"));

        SerializerBuffer ser = new SerializerBuffer();
        clValueAny.serialize(ser, Target.JSON);
        assertThat(ser.toByteArray(), is(Hex.decode("d2029649")));

        ser = new SerializerBuffer();
        clValueAny.serialize(ser, Target.BYTE);
        assertThat(ser.toByteArray(), is(Hex.decode("04000000d202964915")));
    }

    @Test
    void getTypeByName_from_CLTypeData_should_throw_NoSuchTypeException() {
        assertThrows(NoSuchTypeException.class, () -> CLTypeData.getTypeByName("NE"));
    }
}
