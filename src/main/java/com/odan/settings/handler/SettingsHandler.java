package com.odan.settings.handler;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class SettingsHandler implements Handler
{

    public void handle(Context ctx)
    {
        ctx.render("pages/settings.hbs");
    }
}
