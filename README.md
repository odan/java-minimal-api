# Minimal API (Javalin + Guice)

A lightweight Java API using:

- Java 21
- Javalin
- Google Guice (Dependency Injection)
- SmallRye Config (Typesafe Configuration)
- Maven (Build)

## Requirements

Install:

- Java 21+
- Maven 3.9+

Check versions:

```bash
java --version
mvn --version
```

## Project Structure

```text
src/main/java/com/odan
├── Main.java
├── config/
│   ├── AppConfig.java
│   ├── AppModule.java
│   └── AppRoutes.java
├── exception/
│   ├── ApiException.java
│   └── ErrorResponse.java
├── health/
│   └── handler/
│       └── HealthHandler.java
├── home/
│   └── handler/
│       └── HomeHandler.java
├── settings/
│   └── handler/
│       └── SettingsHandler.java
└── user/
    ├── domain/
    │   └── UserEntity.java
    ├── dto/
    │   └── UserResponse.java
    ├── handler/
    │   └── GetUsersHandler.java
    ├── mapper/
    │   └── UserMapper.java
    ├── repository/
    │   └── UserRepository.java
    └── service/
        └── UserService.java

src/main/resources/
├── META-INF/
│   ├── microprofile-config.properties
│   ├── microprofile-config-dev.properties
│   ├── microprofile-config-prod.properties
│   └── microprofile-config-test.properties
├── public/                 # Static assets
├── templates/
│   ├── layouts/
│   │   └── main.hbs
│   └── pages/
│       ├── dashboard.hbs
│       └── settings.hbs
└── logback.xml
```

## Architecture

Dependency flow:

```text
AppRoutes
  -> Handler
      -> Service
          -> Repository
```

Example:

```text
/users
  -> GetUsersHandler
      -> UserService
          -> UserRepository
```

Routes are registered centrally in:

```java
com.odan.config.AppRoutes
```

Example:

```java
app.get("/users", injector.getInstance(GetUsersHandler.class));
```

Handlers are resolved through Guice when a route is registered.

## Compile

Compile sources:

```bash
mvn compile
```

## Run (Development)

Run application:

```bash
mvn exec:java
```

Default server:

```text
http://localhost:8080
```

Test endpoints:

```bash
curl http://localhost:8080/health
```

```bash
curl http://localhost:8080/users
```

## Clean

Remove generated artifacts:

```bash
mvn clean
```

## Build

Create jar:

```bash
mvn package
```

Generated file:

```text
target/minimal-api-1.0.0.jar
```

## Run Built Jar

If jar is executable:

```bash
java -jar target/minimal-api-1.0.0.jar
```

Or use exec:java with profile:

```bash
mvn exec:java -Dsmallrye.config.profile=prod
```

## Clean + Rebuild

```bash
mvn clean package
```

## Run Tests

```bash
mvn test
```

## Code Formatting

This project uses [Spotless](https://github.com/diffplug/spotless) with the Eclipse JDT formatter to enforce consistent Java code style.

Formatting rules are defined in:

```text
spotless.xml
```

Check formatting (fails build if violations found):

```bash
mvn spotless:check
```

Automatically fix formatting violations:

```bash
mvn spotless:apply
```

Fix then verify:

```bash
mvn spotless:apply && mvn spotless:check
```

## Dependency Injection

Guice bindings:

```java
bind(GetUsersHandler.class).in(Singleton.class);
bind(UserService.class).in(Singleton.class);
bind(UserRepository.class).in(Singleton.class);
```

Lazy singleton creation:

- created on first use
- reused afterwards

## Configuration

Configuration files:

```text
src/main/resources/META-INF/*.properties
```

### Priorities

1. System Properties (highest priority → wins over everything)

   Example: `-Dserver.http-port=80`

2. Environment Variables (wins over .env and config files)

   Example: `SERVER_HTTP_PORT=80`

3. `.env` file (wins over config files, but loses against real ENV)

   Example: `SERVER_HTTP_PORT=80`

4. microprofile-config-{profile}.properties (wins over default config file)

   Example: `server.http-port=80`

5. microprofile-config.properties (lowest priority → overridden by all above)

   Example: `server.http-port=80`

Note: `.env` does not override existing system properties.

### Profile

The active configuration profile is set via `smallrye.config.profile`:

```bash
-Dsmallrye.config.profile=prod     # production
-Dsmallrye.config.profile=dev      # development (default)
-Dsmallrye.config.profile=test     # tests
```

The default profile is `dev` (defined in `AppConfig.java`).

## SSL / HTTPS

This application runs on HTTP only. For production deployments, SSL/TLS termination should be handled by a reverse proxy. This approach provides several benefits:

- **No application restart required** when certificates are renewed
- **Centralized certificate management** across multiple applications
- **Automatic certificate renewal** with Let's Encrypt and Certbot
- **Better performance** through connection pooling and HTTP/2 support at the proxy level

Recommended Setup: HAProxy with Let's Encrypt and Certbot

Certbot runs twice daily via systemd timer. When a certificate is renewed, the hook automatically:
- Combines the new certificate and key
- Reloads HAProxy to apply the new certificate

## Useful Maven Commands

Compile:

```
mvn compile
```

Run:

```
mvn exec:java
```

Test:

```
mvn test
```

Clean:

```
mvn clean
```

Build:

```
mvn package
```

Clean + Build:

```
mvn clean package
```

Dependency tree:

```
mvn dependency:tree
```

## Future Extensions

Possible next steps:

- Request validation
- JSON serialization config
- Middleware
- Authentication
- Database integration
- OpenAPI/Swagger