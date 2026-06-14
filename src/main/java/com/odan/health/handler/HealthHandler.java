package com.odan.health.handler;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class HealthHandler implements Handler
{

    public void handle(Context ctx)
    {
        ctx.json(new HealthResponse("UP"));
    }

    public record HealthResponse(String status) {
    }
}
