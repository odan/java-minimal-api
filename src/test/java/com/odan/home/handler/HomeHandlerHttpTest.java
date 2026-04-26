package com.odan.home.handler;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;

import com.odan.HttpTestCase;
import org.junit.jupiter.api.Test;

class HomeHandlerHttpTest extends HttpTestCase {

    @Test
    void shouldReturnHomePage() {
        get("/").then().statusCode(200).body(not(emptyString()));
    }
}
