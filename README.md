![response4j flow](./docs/response4j-flow.svg)
# response4j

A framework-agnostic Java library for standardized API success and error responses aligned with [RFC 9457 Problem Details for HTTP APIs](https://www.rfc-editor.org/rfc/rfc9457) with first-class support for major frameworks such as Spring, Quarkus, Micronaut.

## Features

- **RFC 9457 compliant** — Error responses follow the Problem Details specification
- **Immutable records** — Core models use Java Records for thread-safe, immutable data structures
- **Framework-agnostic core** — Use `response4j-core` with any Java web framework
- **Spring Boot support** — Optional `response4j-spring` module with auto-configuration
- **Quarkus support** — Optional `response4j-quarkus` module with CDI producers and JAX-RS integration
- **Micronaut support** — Optional `response4j-micronaut` module with bean factory, HTTP filter, and exception handler
- **Simple annotations** — `@SuccessResponse` for success wrapping, `@ProblemResponse` for exception mapping
- **Consistent structure** — Success and error responses share predictable JSON shapes

## Requirements

- **Java 21+**
- **Maven 3.6+**

## Compatibility

| response4j | Java | Spring Boot | Quarkus | Micronaut |
|------------|------|-------------|---------|-----------|
| 0.1.0      | 21+  | 3.5.x       | 3.22.x  | 4.10.x    |

## Modules

| Module                 | Description                                                                                                                                              |
|------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| `response4j-core`      | Core models (`ApiResponse`, `ProblemDetail`), annotations, and mappers. No framework dependencies.                                                       |
| `response4j-spring`    | Spring Boot auto-configuration: exception handler, response body advice. Depends on Spring Web MVC and Boot.                                             |
| `response4j-quarkus`   | Quarkus integration: CDI producers for mappers, JAX-RS exception mapper, container response filter. Depends on Quarkus REST (RESTEasy Reactive) and Arc. |
| `response4j-micronaut` | Micronaut integration: bean factory for mappers, HTTP server filter, exception handler. Depends on Micronaut HTTP, server, and inject.                   |

## Installation

### Maven

Add the dependency for your use case.

**Core only** (framework-agnostic):

```xml
<dependency>
    <groupId>io.github.response4j</groupId>
    <artifactId>response4j-core</artifactId>
    <version>0.1.0</version>
</dependency>
```

**Spring Boot** (includes core):

```xml
<dependency>
    <groupId>io.github.response4j</groupId>
    <artifactId>response4j-spring</artifactId>
    <version>0.1.0</version>
</dependency>
```

**Quarkus** (includes core):

```xml
<dependency>
    <groupId>io.github.response4j</groupId>
    <artifactId>response4j-quarkus</artifactId>
    <version>0.1.0</version>
</dependency>
```

**Micronaut** (includes core):

```xml
<dependency>
    <groupId>io.github.response4j</groupId>
    <artifactId>response4j-micronaut</artifactId>
    <version>0.1.0</version>
</dependency>
```

### Building from Source

```bash
git clone https://github.com/iamkavindu/response4j.git
cd response4j
mvn clean install
```

## Usage

### Spring Boot

With `response4j-spring` on the classpath, beans are auto-configured. No extra setup required. Auto-configuration only activates in a web application context (`@ConditionalOnWebApplication`). All registered beans (`ApiResponseMapper`, `ProblemDetailMapper`, `Response4jExceptionHandler`, `Response4jResponseBodyAdvice`) are conditional on `@ConditionalOnMissingBean`, so any of them can be replaced by declaring your own bean of the same type.

#### Success responses

Annotate controller methods or classes with `@SuccessResponse` to wrap return values in a consistent structure:

```java
@RestController
@SuccessResponse  // Applies to all methods in this controller
public class UserController {

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping("/users")
    @SuccessResponse(status = 201, message = "User created successfully")
    public User createUser(@RequestBody UserRequest request) {
        return userService.create(request);
    }

    @GetMapping("/health")
    @SuccessResponse(wrap = false)
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }
}
```

Example success response:

```json
{
  "status": 200,
  "message": "Request successful",
  "timestamp": "2024-01-15T10:30:00Z",
  "data": {
    "id": 1,
    "name": "John Doe"
  }
}
```

#### Error responses

Annotate exception classes with `@ProblemResponse` to control how they are mapped to RFC 9457 Problem Details:

```java
@ProblemResponse(status = 404, title = "User Not Found")
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User with id " + id + " was not found");
    }
}

@ProblemResponse(status = 400, title = "Validation Error", type = "https://api.example.com/errors/validation")
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
```

The example above (`UserNotFoundException`) produces the following response:

```json
{
  "type": "about:blank",
  "title": "User Not Found",
  "status": 404,
  "detail": "User with id 42 was not found"
}
```

Unannotated exceptions produce `Content-Type: application/problem+json` with status 500:

```json
{
  "type": "about:blank",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred"
}
```

#### Problem extensions

Add custom fields to problem details using `@ProblemExtension`. This annotation is repeatable, allowing multiple extensions on the same exception class:

```java
@ProblemResponse(status = 400, title = "Validation Error",
    type = "https://api.example.com/errors/validation")
@ProblemExtension(key = "docs", value = "https://api.example.com/docs/validation")
@ProblemExtension(key = "supportEmail", value = "support@example.com")
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
```

Extension fields appear as top-level properties in the JSON response:

```json
{
  "type": "https://api.example.com/errors/validation",
  "title": "Validation Error",
  "status": 400,
  "detail": "Invalid email format",
  "docs": "https://api.example.com/docs/validation",
  "supportEmail": "support@example.com"
}
```

### Quarkus

With `response4j-quarkus` on the classpath, CDI beans (`ApiResponseMapper`, `ProblemDetailMapper`) are auto-produced. A JAX-RS `ExceptionMapper` and `ContainerResponseFilter` handle exception mapping and `@SuccessResponse` wrapping respectively.

Annotate resource methods or classes with `@SuccessResponse` and exception classes with `@ProblemResponse` (with optional `@ProblemExtension`) the same way as with Spring Boot:

```java
@Path("/users")
@SuccessResponse
public class UserResource {

    @GET
    @Path("/{id}")
    public User getUser(@PathParam("id") Long id) {
        return userService.findById(id);
    }

    @POST
    @SuccessResponse(status = 201, message = "User created successfully")
    public User createUser(UserRequest request) {
        return userService.create(request);
    }
}
```

### Micronaut

With `response4j-micronaut` on the classpath, a bean factory auto-registers `ApiResponseMapper` and `ProblemDetailMapper` when Micronaut HTTP is present. `Response4jHttpServerFilter` applies `@SuccessResponse` wrapping to controller responses, and `Response4jExceptionHandler` maps exceptions to RFC 9457 Problem Details.

Annotate controller methods or classes with `@SuccessResponse` and exception classes with `@ProblemResponse` (with optional `@ProblemExtension`) the same way as with Spring Boot or Quarkus:

```java
@Controller("/users")
@SuccessResponse
public class UserController {

    @Get("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.findById(id);
    }

    @Post
    @SuccessResponse(status = 201, message = "User created successfully")
    public User createUser(@Body UserRequest request) {
        return userService.create(request);
    }
}
```

### Validation Errors

For validation scenarios with multiple field-level errors, use `ProblemDetailError` to represent individual field errors and `ProblemDetail.ofErrors()` to create a problem response containing them:

```java
import io.github.response4j.core.model.ProblemDetail;
import io.github.response4j.core.model.ProblemDetailError;

List<ProblemDetailError> errors = List.of(
    new ProblemDetailError("/email", "must be a valid email address"),
    new ProblemDetailError("/age", "must be at least 18"),
    new ProblemDetailError("username", "is already taken")
);

ProblemDetail problem = ProblemDetail.ofErrors(
    "Validation Failed",
    400,
    "The request contains invalid fields",
    errors
);
```

The resulting JSON response includes an `errors` array as an extension field:

```json
{
  "type": "about:blank",
  "title": "Validation Failed",
  "status": 400,
  "detail": "The request contains invalid fields",
  "errors": [
    {
      "pointer": "/email",
      "detail": "must be a valid email address"
    },
    {
      "pointer": "/age",
      "detail": "must be at least 18"
    },
    {
      "pointer": "username",
      "detail": "is already taken"
    }
  ]
}
```

The `pointer` field can be a JSON Pointer (RFC 6901) like `/email` or `/user/profile/age`, or a simple field name like `username`.

### Core (framework-agnostic)

Use `response4j-core` without Spring for manual mapping and serialization:

```java
// Success response
ApiResponse<User> ok = ApiResponse.ok(user);
ApiResponse<User> created = ApiResponse.created(user);
ApiResponse<?> noContent = ApiResponse.noContent();

// Custom response
ApiResponse<Order> custom = ApiResponse.of(200, "Order processed", order);

// Using ApiResponse.Builder for full control
ApiResponse<User> response = new ApiResponse.Builder<User>()
    .status(200)
    .message("User retrieved")
    .timestamp(Instant.now())
    .data(user)
    .build();

// Problem details
ProblemDetail problem = ProblemDetail.of("Not Found", 404, "Resource not found", null, null);

// Using ProblemDetail.Builder for full control
ProblemDetail notFound = new ProblemDetail.Builder()
    .type(ProblemTypes.ABOUT_BLANK)
    .title("Not Found")
    .status(404)
    .detail("Resource not found")
    .build();

// Validation errors
List<ProblemDetailError> errors = List.of(
    new ProblemDetailError("/email", "must be a valid email address"),
    new ProblemDetailError("/age", "must be at least 18")
);
ProblemDetail validation = ProblemDetail.ofErrors(
    "Validation Failed",
    400,
    "The request contains invalid fields",
    errors
);
```

## API Reference

### `ApiResponse<T>`

| Field       | Type      | Description              |
|-------------|-----------|--------------------------|
| `status`    | `int`     | HTTP status code         |
| `message`   | `String`  | Human-readable message   |
| `timestamp` | `Instant` | UTC timestamp (ISO-8601) |
| `data`      | `T`       | Optional payload         |

Factory methods:

| Method                      | Status | Default message                      |
|-----------------------------|--------|--------------------------------------|
| `of(status, message, data)` | custom | custom                               |
| `empty(status, message)`    | custom | custom (no `data` field in response) |
| `ok(data)`                  | `200`  | `"Request successful"`               |
| `created(data)`             | `201`  | `"Request created successfully"`     |
| `noContent()`               | `204`  | `"No content"` (no `data` field)     |

Use `ApiResponse.Builder<T>` for full field control:

```java
ApiResponse<User> response = new ApiResponse.Builder<User>()
    .status(200)
    .message("User retrieved")
    .timestamp(Instant.now())
    .data(user)
    .build();
```

### `ProblemDetail`

| Field        | Type                  | Description                                                     |
|--------------|-----------------------|-----------------------------------------------------------------|
| `type`       | `URI`                 | Problem type (RFC 9457)                                         |
| `title`      | `String`              | Short summary                                                   |
| `status`     | `int`                 | HTTP status                                                     |
| `detail`     | `String`              | Explanation for this occurrence                                 |
| `instance`   | `String`              | Optional instance URI                                           |
| `extensions` | `Map<String, Object>` | Optional extra fields (serialized as top-level JSON properties) |

Factory methods: `of(title, status, detail, instance, extensions)`, `ofErrors(title, status, detail, errors)`. Both default `type` to `about:blank`.

Use `ProblemDetail.Builder` for full field control, including custom `type` URIs:

```java
ProblemDetail problem = new ProblemDetail.Builder()
    .type(URI.create("https://api.example.com/errors/not-found"))
    .title("Not Found")
    .status(404)
    .detail("Resource not found")
    .instance("/api/users/42")
    .build();
```

### `ProblemDetailError`

| Field     | Type     | Description                                                    |
|-----------|----------|----------------------------------------------------------------|
| `pointer` | `String` | JSON Pointer (RFC 6901) or field name identifying error source |
| `detail`  | `String` | Human-readable explanation of this specific error              |

Used in `ProblemDetail.extensions` under the `"errors"` key for multi-problem responses (validation scenarios).

### `ProblemTypes`

Utility class providing constants for well-known problem type URIs:

| Constant      | Value                                                   | Description                                        |
|---------------|---------------------------------------------------------|----------------------------------------------------|
| `ABOUT_BLANK` | `URI.create("about:blank")`                             | Indicates no specific problem type documentation   |
| `IANA_BASE`   | `"https://iana.org/assignments/http-problem-types#"`    | Base URI for IANA-registered problem types         |

### `ApiResponseMapper`

Maps controller return values to `ApiResponse` instances. Used internally by all framework integration modules.

| Method                  | Description                                                                                                                                                                                                                                |
|-------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `map(data, annotation)` | Maps `data` using `@SuccessResponse` annotation settings. Returns `null` when `wrap = false`, signalling the framework to pass the original body through unchanged. Falls back to `ok(data)` or `noContent()` when `annotation` is `null`. |

### `ProblemDetailMapper`

Maps `Throwable` instances to RFC 9457 `ProblemDetail`. Used internally by all framework integration modules.

| Method                     | Description                                                         |
|----------------------------|---------------------------------------------------------------------|
| `map(exception)`           | Maps `exception` without an instance URI.                           |
| `map(exception, instance)` | Maps `exception` with an instance URI (typically the request path). |

When the exception class is annotated with `@ProblemResponse`, annotation values are used. Blank `title` falls back to the exception class simple name. When `type` is `about:blank` and `title` is blank, `title` is replaced with the HTTP reason phrase per RFC 9457 Section 4.2.1. Unannotated exceptions produce a `500 Internal Server Error` response with `type: about:blank`, `title: "Internal Server Error"`, and the exception message used as the `detail` field.

### `@SuccessResponse`

| Attribute | Default                | Description                                          |
|-----------|------------------------|------------------------------------------------------|
| `status`  | `200`                  | HTTP status code                                     |
| `message` | `"Request successful"` | Message in the response                              |
| `wrap`    | `true`                 | Wrap in `ApiResponse`; if `false`, return body as-is |

### `@ProblemResponse`

| Attribute                 | Default                                   | Description                                                                                                                                                                                                                  |
|---------------------------|-------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `status`                  | `500`                                     | HTTP status code                                                                                                                                                                                                             |
| `title`                   | `""` (falls back to exception class name) | Short summary                                                                                                                                                                                                                |
| `type`                    | `"about:blank"`                           | Problem type URI. When `about:blank` and `title` is blank, title is automatically replaced with the HTTP reason phrase per RFC 9457 Section 4.2.1. When `title` is explicitly set, it is preserved as-is regardless of type. |
| `detail`                  | `""`                                      | Detail text                                                                                                                                                                                                                  |
| `includeExceptionMessage` | `true`                                    | Use exception message as detail when `detail` is blank                                                                                                                                                                       |

### `@ProblemExtension`

| Attribute | Description                                               |
|-----------|-----------------------------------------------------------|
| `key`     | Extension field name (appears as top-level JSON property) |
| `value`   | Extension field value (serialized as string)              |

**Repeatable:** Apply multiple times on the same exception class. The `@ProblemExtensions` container annotation is auto-applied by the compiler.

## Project Structure

```
response4j/
├── response4j-core/          # Framework-agnostic core
│   └── src/main/java/.../
│       ├── annotation/       # @SuccessResponse, @ProblemResponse, @ProblemExtension, @ProblemExtensions
│       ├── mapper/           # ApiResponseMapper, ProblemDetailMapper
│       └── model/            # ApiResponse, ProblemDetail, ProblemDetailError, ProblemTypes
├── response4j-spring/        # Spring Boot integration
│   └── src/main/java/.../
│       ├── autoconfigure/    # Response4jAutoConfiguration
│       ├── advice/           # Response4jResponseBodyAdvice
│       └── handler/          # Response4jExceptionHandler
├── response4j-quarkus/       # Quarkus integration
│   └── src/main/java/.../
│       ├── producer/         # Response4jProducer (CDI)
│       ├── mapper/           # Response4jExceptionMapper
│       └── filter/           # Response4jContainerResponseFilter
├── response4j-micronaut/     # Micronaut integration
│   └── src/main/java/.../
│       ├── factory/          # Response4jFactory (beans)
│       ├── filter/           # Response4jHttpServerFilter
│       └── handler/          # Response4jExceptionHandler
└── pom.xml
```

## License

[Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0)

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Format code with Spotless before building:
   ```bash
   mvn spotless:apply
   ```
   This project enforces [Palantir Java Format](https://github.com/palantir/palantir-java-format) via the [Spotless Maven plugin](https://github.com/diffplug/spotless). The `spotless:check` goal runs automatically during the `compile` phase, so the build will fail if code is not correctly formatted.
5. Run `mvn clean verify`
6. Submit a pull request
