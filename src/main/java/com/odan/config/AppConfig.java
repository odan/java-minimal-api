package com.odan.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

@ConfigMapping(prefix = "")
public interface AppConfig {

    @WithName("smallrye.config.profile")
    @WithDefault("dev")
    String profile();

    App app();

    Server server();

    interface App {
        String name();
        String version();
    }

    interface Server {
        @WithDefault("80")
        int httpPort();

        @WithDefault("443")
        int httpsPort();
    }

}