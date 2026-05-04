package com.odan.user.handler;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import com.odan.testing.HttpTestExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(HttpTestExtension.class)
class GetUsersHandlerHttpTest {

    @Test
    void shouldReturnUsers() {
        get("/users").then()
                .statusCode(200)

                // array size
                .body("$", hasSize(2))

                // first element
                .body("[0].id", equalTo(1))
                .body("[0].username", equalTo("alice"))
                .body("[0].email", equalTo("alice@example.com"))

                // second element
                .body("[1].id", equalTo(2))
                .body("[1].username", equalTo("bob"))
                .body("[1].email", equalTo("bob@example.com"));
    }
}
