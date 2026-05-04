package com.odan.health.handler;

import io.javalin.http.Context;

public class HealthHandler {

    public void handle(Context ctx) {
        ctx.json(new HealthResponse("UP"));
    }

    public record HealthResponse(String status) {
    }
}
