package com.casper.sdk.e2e.steps;

import com.casper.sdk.e2e.utils.CLValueFactory;
import com.casper.sdk.model.clvalue.CLValueOption;
import com.casper.sdk.model.clvalue.cltype.CLTypeData;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author ian@meywood.com
 */
public class OptionValuesStepDefinitin {

    private CLValueOption clValueOption;
    private final CLValueFactory clValueFactory = new CLValueFactory();

    @Given("^that an option value has an empty value$")
    public void thatAnOptionValueHasAnEmptyValue() throws ValueSerializationException {

        clValueOption = new CLValueOption(Optional.empty());
    }

    @Then("^the option value is not present$")
    public void theOptionValueShouldBeInvalid() {
        assertThat(clValueOption.getValue().isPresent(), is(false));
    }

    @And("the option value bytes are {string}")
    public void theOptionValueBytesAre(final String hexBytes) {
        assertThat(clValueOption.getBytes(), is(hexBytes));
    }

    @Given("that an option value has a {string} value of {string}")
    public void thatAnOptionValuesHasAValueOf(final String typeName, final String strValue) throws Exception {

        clValueOption = new CLValueOption(
                Optional.of(clValueFactory.createValue(CLTypeData.getTypeByName(typeName), strValue))
        );
    }

    @Then("the option value is present")
    public void theOptionValueIsPresent() {
        assertThat(clValueOption.getValue().isPresent(), is(true));
    }
}
