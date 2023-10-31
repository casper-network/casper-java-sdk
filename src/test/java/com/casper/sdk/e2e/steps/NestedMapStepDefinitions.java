package com.casper.sdk.e2e.steps;

import com.casper.sdk.model.clvalue.AbstractCLValue;
import com.casper.sdk.model.clvalue.CLValueMap;
import com.casper.sdk.model.clvalue.CLValueString;
import com.casper.sdk.model.clvalue.CLValueU32;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author ian@meywood.com
 */
public class NestedMapStepDefinitions {

    private CLValueMap map;

    @Given("a map with a single key")
    public void aMapWithASingleKey() throws Exception {

       final Map<AbstractCLValue<?,?>, AbstractCLValue<?,?>> innerMap = new LinkedHashMap<>();

        innerMap.put(new CLValueString("One"), new CLValueU32(1L));
        innerMap.put(new CLValueString("Two"), new CLValueU32(2L));
        innerMap.put(new CLValueString("Three"), new CLValueU32(3L));

        map = new CLValueMap(innerMap);

    }

    @When("I call the nested map function")
    public void iCallTheNestedMapFunction() {


    }

    @Then("I should get a map with a single key")
    public void iShouldGetAMapWithASingleKey() {
    }
}
