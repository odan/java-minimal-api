package com.odan.home.handler;

import io.javalin.http.Context;

public class HomeHandler {

    public void handle(Context ctx) {
        ctx.render("pages/dashboard.jte");
    }
}
