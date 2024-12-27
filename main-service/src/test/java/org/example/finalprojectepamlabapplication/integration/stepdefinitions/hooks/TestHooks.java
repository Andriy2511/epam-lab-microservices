package org.example.finalprojectepamlabapplication.integration.stepdefinitions.hooks;

import io.cucumber.java.Before;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class TestHooks {

    public static Response response;
    public static String token;

    @Before
    public void setupTestData()  {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 9000;
    }
}
