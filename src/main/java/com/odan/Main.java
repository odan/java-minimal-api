package com.odan;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.odan.config.AppConfig;
import com.odan.config.AppModule;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main
{

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args)
    {
        logger.info("Server starting");

        Injector injector = Guice.createInjector(new AppModule());
        injector.getInstance(Main.class).start(injector);
    }

    public void start(Injector injector)
    {
        var configuration = injector.getInstance(AppConfig.class);

        logger.info("Environment: {}", configuration.profile());
        logger.info("Version: {}", configuration.app().version());
        logger.info("HTTP Port: {}", configuration.server().httpPort());
        logger.info("HTTPS Port: {}", configuration.server().httpsPort());

        injector.getInstance(Javalin.class).start();
    }
}
