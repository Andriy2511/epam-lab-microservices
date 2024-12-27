package org.example.finalprojectepamlabapplication.integration.stepdefinitions;

import io.cucumber.java.en.Then;
import org.example.finalprojectepamlabapplication.integration.stepdefinitions.hooks.TestHooks;

import static org.hamcrest.Matchers.equalTo;

public class ResponseValidationSteps {

    @Then("the response status should be {int}")
    public void verifyResponseStatus(int statusCode) {
        TestHooks.response.then().statusCode(statusCode);
    }

    @Then("the response should contain message {string}")
    public void verifyResponseMessage(String message) {
        TestHooks.response.then().body("message", equalTo(message));
    }
}
