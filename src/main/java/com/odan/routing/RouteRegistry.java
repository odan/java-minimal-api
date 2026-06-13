package com.odan.routing;

import com.google.inject.Injector;
import com.odan.health.handler.HealthHandler;
import com.odan.home.handler.HomeHandler;
import com.odan.settings.handler.SettingsHandler;
import com.odan.user.handler.GetUsersHandler;
import io.javalin.config.JavalinConfig;

public final class RouteRegistry {

    private RouteRegistry()
    {
    }

    public static void register(JavalinConfig config, Injector injector)
    {
        config.routes.get("/", injector.getInstance(HomeHandler.class));
        config.routes.get("/settings", injector.getInstance(SettingsHandler.class));
        config.routes.get("/health", injector.getInstance(HealthHandler.class));
        config.routes.get("/users", injector.getInstance(GetUsersHandler.class));
    }
}
