package com.odan;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.odan.config.AppConfig;
import com.odan.config.AppModule;
import com.odan.exception.ApiException;
import com.odan.exception.ErrorResponse;
import com.odan.routing.RouteRegistry;
import io.javalin.Javalin;
import io.javalin.community.ssl.SslPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Server starting");

        Injector injector = Guice.createInjector(new AppModule());
        injector.getInstance(Main.class).start(injector);
    }

    public void start(Injector injector) {
        var configuration = injector.getInstance(AppConfig.class);

        logger.info("Environment: {}", configuration.profile());
        logger.info("Version: {}", configuration.app().version());
        logger.info("HTTP Port: {}", configuration.server().httpPort());
        logger.info("HTTPS Port: {}", configuration.server().httpsPort());

        var sslPlugin = new SslPlugin(config -> {
            config.insecurePort = configuration.server().httpPort();
            config.securePort = configuration.server().httpsPort();

            config.pemFromPath("src/main/resources/ssl/cert.pem", "src/main/resources/ssl/key.pem");
        });

        var app = Javalin.create(config -> {
            config.registerPlugin(sslPlugin);

            RouteRegistry.register(config, injector);

            config.routes.exception(ApiException.class, (e, ctx) -> {
                ctx.status(e.getStatusCode());
                ctx.json(new ErrorResponse(e.getMessage()));
            });

            /*
             * config.routes.exception(FileNotFoundException.class, (e, ctx) -> {
             * ctx.status(404); });
             */

            config.routes.error(404, ctx -> {
                ctx.result("Not found 404 message");
            });

        });

        app.start();
    }
}
