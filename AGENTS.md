# Agents Guide

This file is intended for AI coding agents (Cline, Cursor, Copilot, etc.) to quickly understand the architecture, conventions, and patterns used in this project.

## Project Overview

A lightweight Java REST API (`minimal-api`) built with:

- **Java 25** — Language/runtime
- **Javalin 7** — HTTP framework (with HTTPS via `javalin-ssl`)
- **Google Guice 7** — Dependency injection
- **SmallRye Config** — Configuration (supports `.env`, system properties, env vars)
- **Handlebars** — Server-side template engine
- **Jackson** — JSON serialization
- **Logback** — Logging
- **Spotless** — Code formatting (Eclipse JDT formatter)
- **Maven** — Build tool
- **JaCoCo** — Test coverage (report on `mvn verify`)

## Architecture

### Dependency Flow

```
AppRoutes
  -> Handler (implements io.javalin.http.Handler)
      -> Service
          -> Repository
              -> Domain entity
```

For JSON endpoints, services map domain entities to DTOs before returning them to handlers:

```
GetUsersHandler
  -> UserService
      -> UserRepository (returns UserEntity)
      -> UserMapper (UserEntity -> UserResponse)
```

Use mappers to keep sensitive domain fields (e.g. `passwordHash`, `internalAdminFlag`) out of API responses.

### Feature Packages

Code is organized by feature under `com.odan`:

| Package | Purpose |
|---------|---------|
| `config` | Guice module, routes, typed configuration |
| `exception` | API exceptions and error response types |
| `health` | Health check endpoint |
| `home` | Dashboard page |
| `settings` | Settings page |
| `user` | Users API (handler, service, repository, domain, dto, mapper) |
| `util` | Shared utilities (Handlebars renderer) |

### Handler Pattern (Mandatory)

All request handlers **MUST** implement `io.javalin.http.Handler`.

```java
import com.google.inject.Inject;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class MyHandler implements Handler
{

    private final MyService myService;

    @Inject
    public MyHandler(MyService myService)
    {
        this.myService = myService;
    }

    @Override
    public void handle(Context ctx)
    {
        ctx.json(myService.doSomething());
    }
}
```

Simple page handlers may omit constructor injection when they have no dependencies (see `HomeHandler`, `SettingsHandler`, `HealthHandler`).

### Route Registration

Routes are registered in `AppRoutes.register()`, which is called from `AppModule.provideJavalin()`. Handlers are resolved via `injector.getInstance()`; lambda wrappers are not used.

Current routes:

| Method | Path | Handler |
|--------|------|---------|
| GET | `/` | `HomeHandler` |
| GET | `/settings` | `SettingsHandler` |
| GET | `/health` | `HealthHandler` |
| GET | `/users` | `GetUsersHandler` |

When adding a route:

1. Create the handler (and service/repository if needed).
2. Bind the handler (and dependencies) in `AppModule.configure()`.
3. Register the route in `AppRoutes.register()`.

### Dependency Injection (Guice)

- All bindings are defined in `AppModule.java`.
- Handlers, services, repositories, and mappers are bound as singletons.
- Constructor injection with `@Inject` is preferred.
- `Javalin`, `SslPlugin`, `Handlebars`, and `AppConfig` are provided via `@Provides` methods.
- The injector is created in `Main.main()`; `Main.start(Injector)` starts the server.

For tests, `TestModule` overrides `AppModule` via `Modules.override(...).with(new TestModule())`.

## Project Structure

```
src/main/java/com/odan/
├── Main.java
├── config/
│   ├── AppConfig.java          # SmallRye ConfigMapping interface
│   ├── AppModule.java          # Guice bindings and Javalin setup
│   └── AppRoutes.java          # Route definitions
├── exception/
│   ├── ApiException.java
│   └── ErrorResponse.java
├── health/handler/
├── home/handler/
├── settings/handler/
├── user/
│   ├── domain/                 # Internal entities (may contain sensitive fields)
│   ├── dto/                    # API response shapes
│   ├── handler/
│   ├── mapper/
│   ├── repository/
│   └── service/
└── util/
    └── HandlebarsRenderer.java

src/main/resources/
├── META-INF/
│   ├── microprofile-config.properties
│   ├── microprofile-config-prod.properties
│   └── microprofile-config-test.properties
├── logback.xml
├── ssl/                        # Dev TLS certificates (see create-dev-certificate.bat)
├── public/                     # Static assets served at / (css/, js/, etc.)
└── templates/
    ├── layouts/main.hbs
    └── pages/
        ├── dashboard.hbs
        └── settings.hbs

src/test/java/com/odan/
├── config/TestModule.java
├── testing/HttpTestExtension.java
└── */handler/*HttpTest.java

.env                            # Local overrides (gitignored)
pom.xml                         # Maven build and dependencies
spotless.xml                    # Code formatting rules
target/                         # Build output
```

## Coding Conventions

### Code Formatting

Formatted with Spotless using the Eclipse JDT formatter (`spotless.xml`).

**Key rules:**

- Indentation: 4 spaces (no tabs)
- Line width: 120 characters
- Braces: class, constructor, method, enum, and annotation declarations use **next line** style
- Block-level braces (if/for/while/try/catch/switch/lambda) use **end of line** style
- Trailing whitespace: trimmed
- Unused imports: removed (via `removeUnusedImports`)
- Imports: sorted (via `importOrder`)
- Annotations: formatted consistently
- Files: end with a newline

**Run formatting:**

```bash
mvn spotless:apply
mvn spotless:check    # verify only
```

## Response Patterns

- **JSON responses**: Use `ctx.json(object)` — Javalin serializes via Jackson
- **Template rendering**: Use `ctx.render("pages/template.hbs")` — rendered via `HandlebarsRenderer`
- **Error responses**: Throw `ApiException`; handler registered in `AppModule.provideJavalin()` returns `ErrorResponse` JSON
- **404 responses**: Registered via `config.routes.error(404, ...)`
- **Status codes**: Set via `ctx.status(code)` before `ctx.json()` or `ctx.result()`

```java
throw new ApiException(404, "User not found");
// -> {"message":"User not found"} with HTTP 404
```

## HTTPS

The server runs HTTP and HTTPS concurrently via Javalin `SslPlugin`:

- HTTP port: `server.http-port` (default `80`)
- HTTPS port: `server.https-port` (default `443`)
- Dev certificates: `src/main/resources/ssl/localhost.crt` and `.key`
- Generate trusted local certs (Windows, requires OpenSSL + Admin):

```
src\main\resources\ssl\create-dev-certificate.bat
```

## Testing Patterns

- Tests use **REST Assured** for HTTP integration testing.
- **Awaitility** waits for the test server to accept connections.
- `HttpTestExtension` starts the app once per JVM in a daemon thread.
- Test profile is set automatically by Surefire: `smallrye.config.profile=test`.
- Test HTTP port is **8888** (`microprofile-config-test.properties`; HTTPS disabled with port `0`).
- Test Guice module is `TestModule` (currently empty; add test overrides there).

**Example test:**

```java
@ExtendWith(HttpTestExtension.class)
class MyHandlerHttpTest
{

    @Test
    void shouldReturnExpected()
    {
        get("/path")
            .then()
            .statusCode(200)
            .body("field", equalTo("value"));
    }
}
```

**Run tests:**

```bash
mvn test
mvn verify    # includes JaCoCo coverage report
```

## Configuration

Configuration uses SmallRye Config with this priority order (highest wins):

1. System properties (`-Dkey=value`)
2. Environment variables (`KEY=value`)
3. `.env` file (does not override existing system properties)
4. `microprofile-config-{profile}.properties`
5. `microprofile-config.properties`

The active profile is set via `smallrye.config.profile`:

```bash
-Dsmallrye.config.profile=prod     # production
-Dsmallrye.config.profile=dev      # development (default)
-Dsmallrye.config.profile=test     # tests (set automatically in Surefire)
```

The default profile is `dev` (defined in `AppConfig.java`).

Typed configuration is mapped through the `AppConfig` interface (`@ConfigMapping`). Property keys use kebab-case in `.properties` files (e.g. `server.http-port`, `app.name`).

## Build and Run

```bash
mvn compile
mvn exec:java                     # run via Main (uses dev profile)
mvn test
mvn package                       # produces target/minimal-api-1.0.0.jar
mvn verify                        # package + JaCoCo report
```

Windows shortcuts: `run.bat`, `build.bat`.

CI (GitHub Actions) runs `mvn -B package` on JDK 25 for pushes/PRs to `main`.

## Key Files to Reference

| File | Purpose |
|------|---------|
| `src/main/java/com/odan/Main.java` | Application entry point |
| `src/main/java/com/odan/config/AppModule.java` | Guice bindings, Javalin/SSL/Handlebars setup |
| `src/main/java/com/odan/config/AppRoutes.java` | All route definitions |
| `src/main/java/com/odan/config/AppConfig.java` | Typed configuration mapping |
| `src/main/java/com/odan/util/HandlebarsRenderer.java` | Template rendering with layout helpers |
| `src/main/java/com/odan/exception/ApiException.java` | Typed API errors with status codes |
| `src/main/resources/templates/layouts/main.hbs` | Main layout template (Tabler UI) |
| `src/main/resources/templates/pages/dashboard.hbs` | Home/dashboard page |
| `src/test/java/com/odan/testing/HttpTestExtension.java` | Test server lifecycle |
| `src/test/java/com/odan/config/TestModule.java` | Test Guice overrides |
| `spotless.xml` | Code formatting rules |
| `pom.xml` | Dependencies and plugin configuration |
