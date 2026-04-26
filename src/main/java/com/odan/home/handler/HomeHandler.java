package com.odan.home.handler;

import io.javalin.http.Context;

public class HomeHandler {

    public void handle(Context ctx) {
        ctx.json(new HomeResponse("Minimal API", "running", "1.0.0"));
    }

    public record HomeResponse(String application, String status, String version) {
    }
}
