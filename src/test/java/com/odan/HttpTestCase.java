package com.odan;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import com.odan.config.Configuration;
import java.time.Duration;
import java.net.Socket;

import static org.awaitility.Awaitility.await;

public abstract class HttpTestCase {

    private static boolean started = false;

    @BeforeAll
    static void startServer() {
        if (started) {
            return;
        }
        
        System.setProperty("app.env", "test");

        var config = new Configuration();
        int port = config.getHttpPort();

        var appThread = new Thread(() -> Main.main(new String[] {}));
        appThread.setDaemon(true);
        appThread.start();

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        await().atMost(Duration.ofSeconds(10)).pollInterval(Duration.ofMillis(250)).until(() -> {
            try (var _ = new Socket("localhost", port)) {
                return true;
            } catch (Exception _) {
                return false;
            }
        });

        started = true;
    }

}