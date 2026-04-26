package com.odan.routing;

import com.google.inject.Injector;
import com.odan.health.handler.HealthHandler;
import com.odan.home.handler.HomeHandler;
import com.odan.user.handler.GetUsersHandler;
import io.javalin.config.JavalinConfig;

public final class RouteRegistry {

    private RouteRegistry() {
    }

    public static void register(JavalinConfig config, Injector injector) {
        config.routes.get("/", ctx -> injector.getInstance(HomeHandler.class).handle(ctx));

        config.routes.get("/health", ctx -> injector.getInstance(HealthHandler.class).handle(ctx));

        config.routes.get("/users", ctx -> injector.getInstance(GetUsersHandler.class).handle(ctx));
    }
}
