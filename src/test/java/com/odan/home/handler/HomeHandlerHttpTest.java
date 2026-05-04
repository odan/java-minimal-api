package com.odan.home.handler;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;

import com.odan.testing.HttpTestExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(HttpTestExtension.class)
class HomeHandlerHttpTest {

    @Test
    void shouldReturnHomePage() {
        get("/").then().statusCode(200).body(not(emptyString()));
    }
}
