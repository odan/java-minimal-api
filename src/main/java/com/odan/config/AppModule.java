package com.odan.config;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.odan.util.HandlebarsRenderer;
import io.javalin.Javalin;
import io.javalin.rendering.FileRenderer;
import io.smallrye.config.SmallRyeConfig;
import io.smallrye.config.SmallRyeConfigBuilder;
import org.slf4j.LoggerFactory;

public class AppModule extends AbstractModule
{

    @Override
    protected void configure()
    {
        bind(FileRenderer.class).to(HandlebarsRenderer.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    public SmallRyeConfig provideSmallRyeConfig()
    {
        return new SmallRyeConfigBuilder().addDefaultInterceptors()// extra
                .addDiscoveredInterceptors() // extra
                .addDefaultSources()
                .addDiscoveredSources()
                .addDiscoveredConverters()
                .withMapping(AppConfig.class)
                .build();
    }

    @Provides
    @Singleton
    public AppConfig provideConfiguration(SmallRyeConfig config)
    {
        return config.getConfigMapping(AppConfig.class);
    }

    @Provides
    @Singleton
    public Javalin provideJavalin(Injector injector, FileRenderer fileRenderer, AppRoutes routes)
    {
        var javalin = Javalin.create(config -> {
            config.fileRenderer(fileRenderer);
            config.staticFiles.add("/public");
            routes.register(config);
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LoggerFactory.getLogger(AppModule.class).info("Server shutting down");
            javalin.stop();
        }));

        return javalin;
    }

    @Provides
    @Singleton
    public Handlebars provideHandlebars()
    {
        return new Handlebars(new ClassPathTemplateLoader("/templates", ".hbs"));
    }
}
