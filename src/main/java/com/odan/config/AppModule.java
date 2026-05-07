package com.odan.config;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.odan.exception.ApiException;
import com.odan.exception.ErrorResponse;
import com.odan.health.handler.HealthHandler;
import com.odan.home.handler.HomeHandler;
import com.odan.routing.RouteRegistry;
import com.odan.user.handler.GetUsersHandler;
import com.odan.user.mapper.UserMapper;
import com.odan.user.repository.UserRepository;
import com.odan.user.service.UserService;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.DirectoryCodeResolver;
import io.javalin.Javalin;
import io.javalin.community.ssl.SslPlugin;
import io.javalin.rendering.template.JavalinJte;
import io.smallrye.config.SmallRyeConfig;
import io.smallrye.config.SmallRyeConfigBuilder;
import java.nio.file.Path;

public class AppModule extends AbstractModule {

    @Override
    protected void configure() {
        // bind(Configuration.class).in(Singleton.class);

        bind(HomeHandler.class).in(Singleton.class);
        bind(HealthHandler.class).in(Singleton.class);
        bind(GetUsersHandler.class).in(Singleton.class);

        bind(UserService.class).in(Singleton.class);
        bind(UserMapper.class).in(Singleton.class);
        bind(UserRepository.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    public SmallRyeConfig provideSmallRyeConfig() {
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
    public AppConfig provideConfiguration(SmallRyeConfig config) {
        return config.getConfigMapping(AppConfig.class);
    }

    @Provides
    @Singleton
    public Javalin provideJavalin(Injector injector, SslPlugin sslPlugin, JavalinJte jte) {
        return Javalin.create(config -> {
            config.fileRenderer(jte);
            config.staticFiles.add("/public");
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
    }

    @Provides
    @Singleton
    public SslPlugin provideSslPlugin(AppConfig appConfig) {
        return new SslPlugin(config -> {
            config.insecurePort = appConfig.server().httpPort();
            config.securePort = appConfig.server().httpsPort();

            config.pemFromPath("src/main/resources/ssl/cert.pem", "src/main/resources/ssl/key.pem");
        });
    }

    @Provides
    @Singleton
    public JavalinJte provideJavalinJte(TemplateEngine templateEngine) {
        return new JavalinJte(templateEngine);
    }

    @Provides
    @Singleton
    public TemplateEngine provideTemplateEngine(AppConfig config) {
        if (config.profile().equals("prod")) {
            // Us precompiled templates in production for better performance
            return TemplateEngine.createPrecompiled(ContentType.Html);
        }

        // Use dynamic template compilation in development for easier debugging
        DirectoryCodeResolver codeResolver = new DirectoryCodeResolver(Path.of("src/main/jte"));

        return TemplateEngine.create(codeResolver, Path.of("target/jte-classes"), ContentType.Html,
                getClass().getClassLoader());
    }
}
