package com.odan.health.handler;

import static io.restassured.RestAssured.get;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.odan.health.handler.HealthHandler.HealthResponse;
import com.odan.testing.HttpTestExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(HttpTestExtension.class)
class HealthHandlerHttpTest {

    @Test
    void shouldReturnHealthStatus() {
        var response = get("/health")
                .then()
                .statusCode(200)
                .extract()
                .as(HealthResponse.class);

        assertEquals("UP", response.status());
    }
}
