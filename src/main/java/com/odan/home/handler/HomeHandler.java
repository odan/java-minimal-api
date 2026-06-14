package com.odan.home.handler;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class HomeHandler implements Handler
{

    public void handle(Context ctx)
    {
        ctx.render("pages/dashboard.hbs");
    }
}
