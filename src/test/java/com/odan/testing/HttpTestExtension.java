package com.odan.testing;

import static org.awaitility.Awaitility.await;

import com.google.inject.Guice;
import com.google.inject.util.Modules;
import com.odan.Main;
import com.odan.config.AppModule;
import com.odan.config.TestModule;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import java.time.Duration;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public final class HttpTestExtension implements BeforeAllCallback
{

    private static boolean started = false;

    @Override
    public void beforeAll(ExtensionContext context)
    {
        if (started) {
            return;
        }

        var injector = Guice.createInjector(Modules.override(new AppModule()).with(new TestModule()));

        var app = injector.getInstance(Main.class);
        var javalin = injector.getInstance(Javalin.class);

        app.start(injector);

        // Wait until server is really running and has a port
        await().atMost(Duration.ofSeconds(10))
                .pollInterval(Duration.ofMillis(250))
                .until(() -> javalin.port() > 0);

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = javalin.port();

        started = true;
    }
}
