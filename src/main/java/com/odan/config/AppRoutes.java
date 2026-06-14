package com.odan.config;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.odan.exception.ApiException;
import com.odan.exception.ErrorResponse;
import com.odan.health.handler.HealthHandler;
import com.odan.home.handler.HomeHandler;
import com.odan.settings.handler.SettingsHandler;
import com.odan.user.handler.GetUsersHandler;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Handler;

public final class AppRoutes
{

    private final Injector injector;

    @Inject
    public AppRoutes(Injector injector)
    {
        this.injector = injector;
    }

    public void register(JavalinConfig config)
    {
        var routes = config.routes;

        routes.get("/", of(HomeHandler.class));
        routes.get("/settings", of(SettingsHandler.class));
        routes.get("/health", of(HealthHandler.class));
        routes.get("/users", of(GetUsersHandler.class));

        routes.exception(ApiException.class, (e, ctx) -> {
            ctx.status(e.getStatusCode());
            ctx.json(new ErrorResponse(e.getMessage()));
        });

        // catch-all
        routes.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace(); // or use proper logger
            ctx.status(500).json(new ErrorResponse("Internal Server Error"));
        });

        routes.error(404, ctx -> ctx.json(new ErrorResponse("Not found")));
        routes.error(500, ctx -> ctx.json(new ErrorResponse("Internal Server Error")));
    }

    private Handler of(Class<? extends Handler> clazz)
    {
        return ctx -> injector.getInstance(clazz).handle(ctx);
    }
}
