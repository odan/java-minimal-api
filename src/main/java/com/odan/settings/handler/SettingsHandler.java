package com.odan.settings.handler;

import io.javalin.http.Context;

public class SettingsHandler {

    public void handle(Context ctx) {
        ctx.render("pages/settings.jte");
    }
}
