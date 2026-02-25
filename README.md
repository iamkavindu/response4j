# response4j

A framework-agnostic Java library for standardized API error responses aligned with [RFC 7807 Problem Details for HTTP APIs](https://www.rfc-editor.org/rfc/rfc7807).

## Features

- **RFC 7807 compliant** — Error responses follow the Problem Details specification
- **Framework-agnostic core** — Use `response4j-core` with any Java web framework
- **Spring Boot support** — Optional `response4j-spring` module with auto-configuration
- **Simple annotations** — `@SuccessResponse` for success wrapping, `@ProblemResponse` for exception mapping
- **Consistent structure** — Success and error responses share predictable JSON shapes

## Requirements

- **Java 21+**
- **Maven 3.6+**

## Modules

| Module | Description |
|--------|-------------|
| `response4j-core` | Core models (`ApiResponse`, `ProblemDetail`), annotations, and mappers. No framework dependencies. |
| `response4j-spring` | Spring Boot auto-configuration: exception handler, response body advice. Depends on Spring Web MVC and Boot. |

## Installation

### Maven

Add the dependency for your use case.

**Core only** (framework-agnostic):

```xml
<dependency>
    <groupId>io.github.iamkavindu</groupId>
    <artifactId>response4j-core</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```

**Spring Boot** (includes core):

```xml
<dependency>
    <groupId>io.github.iamkavindu</groupId>
    <artifactId>response4j-spring</artifactId>
    <version>0.1.0-SNAPSHOT</version>
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

With `response4j-spring` on the classpath, beans are auto-configured. No extra setup required.

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

Annotate exception classes with `@ProblemResponse` to control how they are mapped to RFC 7807 Problem Details:

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

Unhandled exceptions produce `Content-Type: application/problem+json` with status 500 when not annotated:

```json
{
  "type": "about:blank",
  "title": "User Not Found",
  "status": 404,
  "detail": "User with id 42 was not found"
}
```

### Core (framework-agnostic)

Use `response4j-core` without Spring for manual mapping and serialization:

```java
// Success response
ApiResponse<User> response = ApiResponse.ok(user);
ApiResponse<User> created = ApiResponse.created(user);
ApiResponse<?> noContent = ApiResponse.noContent();

// Custom response
ApiResponse<Order> custom = ApiResponse.of(200, "Order processed", order);

// Problem details
ProblemDetail problem = ProblemDetail.of(404, "Not Found", "Resource not found");
```

## API Reference

### `ApiResponse<T>`

| Field | Type | Description |
|-------|------|-------------|
| `status` | `int` | HTTP status code |
| `message` | `String` | Human-readable message |
| `timestamp` | `Instant` | UTC timestamp (ISO-8601) |
| `data` | `T` | Optional payload |

Factory methods: `of(status, message, data)`, `empty(status, message)`, `ok(data)`, `created(data)`, `noContent()`.

### `ProblemDetail`

| Field | Type | Description |
|-------|------|-------------|
| `type` | `URI` | Problem type (RFC 7807) |
| `title` | `String` | Short summary |
| `status` | `int` | HTTP status |
| `detail` | `String` | Explanation for this occurrence |
| `instance` | `String` | Optional instance URI |
| `extensions` | `Map<String, Object>` | Optional extra fields |

### `@SuccessResponse`

| Attribute | Default | Description |
|-----------|---------|-------------|
| `status` | `200` | HTTP status code |
| `message` | `"Request successful"` | Message in the response |
| `wrap` | `true` | Wrap in `ApiResponse`; if `false`, return body as-is |

### `@ProblemResponse`

| Attribute | Default | Description |
|-----------|---------|-------------|
| `status` | `500` | HTTP status code |
| `title` | `""` (falls back to exception class name) | Short summary |
| `type` | `"about:blank"` | Problem type URI |
| `detail` | `""` | Detail text |
| `includeExceptionMessage` | `true` | Use exception message as detail when `detail` is blank |

## Project Structure

```
response4j/
├── response4j-core/          # Framework-agnostic core
│   └── src/main/java/.../
│       ├── annotation/       # @SuccessResponse, @ProblemResponse
│       ├── mapper/           # ApiResponseMapper, ProblemDetailMapper
│       └── model/            # ApiResponse, ProblemDetail
├── response4j-spring/        # Spring Boot integration
│   └── src/main/java/.../
│       ├── autoconfigure/    # Response4jAutoConfiguration
│       ├── advice/           # Response4jResponseBodyAdvice
│       └── handler/          # Response4jExceptionHandler
└── pom.xml
```

## License

[Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0)

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Run `mvn clean verify`
5. Submit a pull request
