# Agents Guide

This file is intended for AI coding agents (Cline, Cursor, Copilot, etc.) to quickly understand the architecture, conventions, and patterns used in this project.

## Project Overview

A lightweight Java REST API built with:

- **Javalin 7** — HTTP framework
- **Google Guice 7** — Dependency injection
- **Spotless** — Code formatting (Eclipse JDT formatter)
- **SmallRye Config** — Configuration (supports .env, system properties, env vars)
- **Handlebars** — Server-side template engine
- **Maven** — Build tool

## Architecture

### Dependency Flow

```
AppRoutes
  -> Handler (implements io.javalin.http.Handler)
      -> Service
          -> Repository
```

### Handler Pattern (Mandatory)

All request handlers **MUST** implement `io.javalin.http.Handler`.

```java
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class MyHandler implements Handler {

    private final MyService myService;

    @Inject
    public MyHandler(MyService myService) {
        this.myService = myService;
    }

    @Override
    public void handle(Context ctx) {
        ctx.json(myService.doSomething());
    }
}
```

### Route Registration

Routes are registered directly in `AppRoutes` using `injector.getInstance()`; lambda wrappers are not used.

Handlers are resolved lazily through Guice when a route is invoked.

### Dependency Injection (Guice)

- All bindings are defined in `AppModule.java`.
- Handlers and services are bound as singletons.
- Constructor injection with `@Inject` is preferred.
- The injector is created once in `Main.java` and passed to `RouteRegistry`.

## Project Structure

 - `src/main/java/com/odan`: Core Java source code
 - `src/main/resources`: Configuration resources
   - `public`: Static assets
     - `css`: CSS files
     - `js`: JavaScript files
   - `ssl`: SSL certificates
   - `templates`: Handlebars template directory
     - `layouts`: Layout templates
     - `pages`: Template pages
   - `META-INF`: Configuration files
 - `src/test/java/com/odan`: Test code

Project build and tooling:
- `pom.xml` — Maven build configuration and dependencies
- `spotless.xml` — Code formatting rules
- `target` — Build output directory

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

- **JSON responses**: Use `ctx.json(object)` — Javalin serializes to JSON using Jackson automatically
- **Template rendering**: Use `ctx.render("pages/template.hbs")` — rendered via Handlebars
- **Error responses**: Exception handlers are registered in `AppModule.provideJavalin()` using `config.routes.exception()`
- **Status codes**: Set via `ctx.status(code)` before `ctx.json()` or `ctx.result()`

## Testing Patterns

- Tests use **REST Assured** for HTTP integration testing.
- A JUnit extension (`HttpTestExtension`) manages the test server lifecycle.
- Test server runs on port 8090.
- Test profile is set via system property `smallrye.config.profile=test`.
- Test Guice module is `TestModule`.

**Example test:**
```java
@ExtendWith(HttpTestExtension.class)
class MyHandlerTest {

    @Test
    void shouldReturnExpected() {
        get("/path")
            .then()
            .statusCode(200)
            .body("field", equalTo("value"));
    }
}
```

## Configuration

Configuration uses SmallRye Config with this priority order:

1. System properties (`-Dkey=value`)
2. Environment variables (`KEY=value`)
3. `.env` file
4. `microprofile-config-{profile}.properties`
5. `microprofile-config.properties`

The active profile is set via `smallrye.config.profile`:

```bash
-Dsmallrye.config.profile=prod     # production
-Dsmallrye.config.profile=dev      # development (default)
-Dsmallrye.config.profile=test     # tests
```

The default profile is `dev` (defined in `AppConfig.java`).

## Key Files to Reference

| File | Purpose |
|------|---------|
| `src/main/java/com/odan/Main.java` | Application entry point |
| `src/main/java/com/odan/config/AppModule.java` | Guice bindings and provider methods |
| `src/main/java/com/odan/routing/AppRoutes.java` | All route definitions |
| `src/main/resources/templates/layouts/main.hbs` | Main layout template |
| `src/main/resources/templates/pages/index.hbs` | Example page template |
| `spotless.xml` | Code formatting rules |
| `pom.xml` | Dependencies and plugin configuration |
