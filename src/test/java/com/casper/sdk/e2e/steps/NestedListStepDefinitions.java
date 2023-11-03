package com.casper.sdk.e2e.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

/**
 * @author ian@meywood.com
 */
public class NestedListStepDefinitions {
    @Given("^a list is created with U(\\d+) values of \\((\\d+), (\\d+), (\\d+)\\)$")
    public void aListIsCreatedWithUValuesOf(int arg0, int arg1, int arg2, int arg3) {
    }

    @Then("^the list's bytes are \"([^\"]*)\"$")
    public void theListSBytesAre(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @And("^the list's length is (\\d+)$")
    public void theListSLengthIs(int arg0) {
    }

    @And("^the list's \"([^\"]*)\" item is a CLValue with U(\\d+) value of (\\d+)$")
    public void theListSItemIsACLValueWithUValueOf(String arg0, int arg1, int arg2) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }
}
