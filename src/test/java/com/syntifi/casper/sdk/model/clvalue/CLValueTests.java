package com.syntifi.casper.sdk.model.clvalue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.syntifi.casper.sdk.exception.DynamicInstanceException;
import com.syntifi.casper.sdk.exception.NoSuchTypeException;
import com.syntifi.casper.sdk.model.clvalue.cltype.CLTypeAny;
import com.syntifi.casper.sdk.model.clvalue.cltype.CLTypeData;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CLValueTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(CLValueTests.class);

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
    void getTypeBySerializationTag_should_return_correct_CLTypeData() throws DynamicInstanceException, NoSuchTypeException {
        for (CLTypeData typeData : CLTypeData.values()) {
            assertEquals(typeData, CLTypeData.getTypeBySerializationTag(typeData.getSerializationTag()));
        }
    }

    @Test
    void createCLTypeFromCLTypeData_should_return_correct_CLType() throws DynamicInstanceException, NoSuchTypeException {
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
    void getTypeByName_from_CLTypeData_should_throw_NoSuchTypeException() {
        assertThrows(NoSuchTypeException.class, () -> CLTypeData.getTypeByName("NE"));
    }
}
