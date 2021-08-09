package com.syntifi.casper.sdk.demo.quarkus.api;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class BlockResourceTest {

    @Test
    public void testHelloEndpoint() {
        given().when().get("/block").then().statusCode(200);
    }

}