package com.odan.config;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.odan.exception.ApiException;
import com.odan.exception.ErrorResponse;
import com.odan.health.handler.HealthHandler;
import com.odan.home.handler.HomeHandler;
import com.odan.settings.handler.SettingsHandler;
import com.odan.user.handler.GetUsersHandler;
import com.odan.user.mapper.UserMapper;
import com.odan.user.repository.UserRepository;
import com.odan.user.service.UserService;
import com.odan.util.HandlebarsRenderer;
import io.javalin.Javalin;
import io.javalin.rendering.FileRenderer;
import io.smallrye.config.SmallRyeConfig;
import io.smallrye.config.SmallRyeConfigBuilder;

public class AppModule extends AbstractModule
{

    @Override
    protected void configure()
    {
        bind(FileRenderer.class).to(HandlebarsRenderer.class).in(Singleton.class);

        bind(HomeHandler.class).in(Singleton.class);
        bind(SettingsHandler.class).in(Singleton.class);
        bind(HealthHandler.class).in(Singleton.class);
        bind(GetUsersHandler.class).in(Singleton.class);

        bind(UserService.class).in(Singleton.class);
        bind(UserMapper.class).in(Singleton.class);
        bind(UserRepository.class).in(Singleton.class);
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
    public Javalin provideJavalin(Injector injector, FileRenderer fileRenderer)
    {
        return Javalin.create(config -> {
            config.fileRenderer(fileRenderer);
            config.staticFiles.add("/public");

            AppRoutes.register(config, injector);

            config.routes.exception(ApiException.class, (e, ctx) -> {
                ctx.status(e.getStatusCode());
                ctx.json(new ErrorResponse(e.getMessage()));
            });

            config.routes.error(404, ctx -> {
                ctx.result("Not found");
            });

        });
    }

    @Provides
    @Singleton
    public Handlebars provideHandlebars()
    {
        return new Handlebars(new ClassPathTemplateLoader("/templates", ".hbs"));
    }
}
