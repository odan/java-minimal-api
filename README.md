# Minimal API (Javalin + Guice)

A lightweight Java API using:

- Java 21
- Javalin (with HTTPS)
- Google Guice (Dependency Injection)
- Typesafe Config 
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
│   ├── AppModule.java
│   └── Configuration.java
├── exception/
│   └── ApiException.java
├── health/
│   └── handler/
│       └── HealthHandler.java
├── routing/
│   └── RouteRegistry.java
└── user/
    ├── domain/
    │   └── User.java
    ├── handler/
    │   └── GetUsersHandler.java
    ├── repository/
    │   └── UserRepository.java
    └── service/
        └── UserService.java

src/main/resources/
└── application.conf
```

## Architecture

Dependency flow:

```text
RouteRegistry
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
com.odan.routing.RouteRegistry
```

Example:

```java
app.get("/users", ctx ->
    injector.getInstance(GetUsersHandler.class).handle(ctx)
);
```

Handlers are lazily resolved through Guice when a route is hit.

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

If using dependencies outside fat-jar packaging:

```bash
mvn exec:java -Dapp.env=prod
```

## Clean + Rebuild

```bash
mvn clean package
```

## Run Tests

```bash
mvn test
```

## Format Java Files

```bash
mvn checkstyle:check
```

Verify formatting (fails build if bad)

```
mvn spotless:check
```

Automatically fix the code

```
mvn spotless:apply
```

Fix then verify

```
mvn spotless:apply spotless:check
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

## HTTPS certificate

Create SSL development certificate

```
openssl req -x509 -newkey rsa:4096 -keyout src/main/resources/ssl/key.pem -out src/main/resources/ssl/cert.pem -days 365 -nodes
```

Enter "localhost" for the domain.


## Future Extensions

Possible next steps:

- Request validation
- JSON serialization config
- Middleware
- Authentication
- Database integration
- OpenAPI/Swagger