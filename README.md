# Minimal API (Javalin + Guice)

A lightweight Java API using:

- Java 21
- Javalin
- Google Guice
- Typesafe Config
- Maven

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

---

## Requirements

Install:

- Java 21+
- Maven 3.9+

Check versions:

```bash
java --version
mvn --version
```

---

## Compile

Compile sources:

```bash
mvn compile
```

---

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

---

## Clean

Remove generated artifacts:

```bash
mvn clean
```

---

## Build

Create jar:

```bash
mvn package
```

Generated file:

```text
target/minimal-api-1.0.0.jar
```

---

## Run Built Jar

If jar is executable:

```bash
java -jar target/minimal-api-1.0.0.jar
```

If using dependencies outside fat-jar packaging:

```bash
mvn exec:java -Dapp.env=prod
```

---

## Clean + Rebuild

```bash
mvn clean package
```

---

## Run Tests

```bash
mvn test
```

## Format Java Files

```bash
mvn formatter:format
mvn formatter:validate
```

```bash
mvn checkstyle:check
```

verifies formatting (fails build if bad)

```
mvn spotless:check
```

automatically fix your code

```
mvn spotless:apply
```

fix then verify

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

---

## Configuration

Configuration file:

```text
src/main/resources/application.conf
```

Example:

```hocon
server.port=8080
```

---

## Useful Maven Commands

Compile:

```bash
mvn compile
```

Run:

```bash
set APP_ENV=dev
mvn exec:java
```

Priorities:

```
Env Vars / System Properties
-> application-{env}.properties
-> META-INF/microprofile-config.properties
```

Test:

```bash
mvn test
```

Clean:

```bash
mvn clean
```

Build:

```bash
mvn package
```

Clean + Build:

```bash
mvn clean package
```

Dependency tree:

```bash
mvn dependency:tree
```

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