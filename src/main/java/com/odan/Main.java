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
        var config = injector.getInstance(AppConfig.class);

        logger.info("Environment: {}", config.profile());
        logger.info("Version: {}", config.appVersion());
        injector.getInstance(Javalin.class).start(config.serverHttpPort());
    }
}
