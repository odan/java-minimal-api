package com.odan.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

@ConfigMapping(prefix = "")
public interface AppConfig
{

    @WithName("smallrye.config.profile")
    @WithDefault("dev")
    String profile();

    @WithName("app.name")
    @WithDefault("Minimal API")
    String appName();

    @WithName("app.version")
    @WithDefault("1.0.0")
    String appVersion();

    @WithName("server.http-port")
    @WithDefault("8080")
    int serverHttpPort();
}
