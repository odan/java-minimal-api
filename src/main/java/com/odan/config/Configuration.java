package com.odan.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.github.cdimascio.dotenv.Dotenv;

public class Configuration {

    private final Config config;

    public Configuration() {
        var dotenv = Dotenv.configure().ignoreIfMissing().load();

        dotenv.entries().forEach(entry -> {
            if (System.getProperty(entry.getKey()) == null) {
                System.setProperty(entry.getKey(), entry.getValue());
            }
        });

        var environment = System.getProperty("app.env", "dev");

        var environmentConfig = ConfigFactory.parseResources("application-" + environment + ".properties");

        this.config = ConfigFactory.systemProperties()
                .withFallback(environmentConfig)
                .withFallback(ConfigFactory.load())
                .resolve();
    }

    public int getHttpPort() {
        return config.getInt("server.httpPort");
    }

    public int getHttpsPort() {
        return config.getInt("server.httpsPort");
    }

    public String getAppName() {
        return config.getString("app.name");
    }

    public String getAppVersion() {
        return config.getString("app.version");
    }

    public String getEnv() {
        return config.getString("app.env");
    }

    public String getSslCertPath() {
        return config.getString("ssl.certPath");
    }

    public String getSslKeyPath() {
        return config.getString("ssl.keyPath");
    }
}
