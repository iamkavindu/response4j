package io.github.response4j.core.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.response4j.core.annotation.SuccessResponse;
import java.time.Instant;

/**
 * Standardized envelope for successful API responses.
 * <p>
 * This immutable record provides a consistent structure for wrapping HTTP response data
 * across different Java web frameworks (Spring Boot, Quarkus, Micronaut). It includes
 * status code, message, timestamp, and optional payload.
 * <p>
 * Instances are typically created via static factory methods ({@link #ok(Object)},
 * {@link #created(Object)}, {@link #noContent()}) or through the fluent {@link Builder}.
 * Framework integration modules automatically wrap controller responses when annotated
 * with {@link SuccessResponse}.
 * <p>
 * This record is thread-safe and immutable. JSON serialization is handled by Jackson
 * annotations, with null fields excluded from the output.
 *
 * @param <T> the type of the response payload
 * @param status the HTTP status code (e.g., 200, 201, 204)
 * @param message human-readable response message describing the result
 * @param timestamp UTC timestamp in ISO-8601 format (yyyy-MM-dd'T'HH:mm:ss.SSS'Z') indicating when the response was created
 * @param data the response payload of type {@code T}; may be {@code null} for no-content responses
 * @see SuccessResponse
 * @see Builder
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        int status,
        String message,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        Instant timestamp,

        T data) {
    /**
     * Builder for constructing {@link ApiResponse} instances using a fluent API.
     * <p>
     * This builder allows step-by-step construction of an {@code ApiResponse} with
     * explicit control over all fields. For common use cases, prefer the static
     * factory methods ({@link ApiResponse#ok(Object)}, {@link ApiResponse#created(Object)}, etc.)
     * which provide sensible defaults.
     *
     * @param <T> the type of the response payload
     * @see ApiResponse
     */
    public static class Builder<T> {
        private int status;
        private String message;
        private Instant timestamp;
        private T data;

        /**
         * Sets the HTTP status code.
         *
         * @param status the HTTP status code (e.g., 200, 201, 204)
         * @return this builder for method chaining
         */
        public Builder<T> status(int status) {
            this.status = status;
            return this;
        }

        /**
         * Sets the human-readable response message.
         *
         * @param message the message describing the response
         * @return this builder for method chaining
         */
        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        /**
         * Sets the timestamp for when the response was created.
         *
         * @param timestamp the UTC timestamp; if not set, factory methods typically use {@link Instant#now()}
         * @return this builder for method chaining
         */
        public Builder<T> timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        /**
         * Sets the response payload.
         *
         * @param data the payload of type {@code T}; may be {@code null} or an empty collection for no-content responses
         * @return this builder for method chaining
         */
        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        /**
         * Constructs an immutable {@link ApiResponse} with the configured values.
         *
         * @return a new {@code ApiResponse} instance
         */
        public ApiResponse<T> build() {
            return new ApiResponse<>(status, message, timestamp, data);
        }
    }

    /**
     * Creates an {@code ApiResponse} with the specified status, message, and data.
     * <p>
     * The timestamp is automatically set to the current instant ({@link Instant#now()}).
     * This is a general-purpose factory method; for common HTTP status codes, prefer
     * the convenience methods like {@link #ok(Object)}, {@link #created(Object)}, or
     * {@link #noContent()}.
     *
     * @param <T> the type of the response payload
     * @param status the HTTP status code (e.g., 200, 201, 204)
     * @param message the human-readable response message
     * @param data the response payload; may be {@code null}
     * @return a new {@code ApiResponse} instance with the current timestamp
     * @see #ok(Object)
     * @see #created(Object)
     * @see #noContent()
     */
    public static <T> ApiResponse<T> of(int status, String message, T data) {
        return new ApiResponse.Builder<T>()
                .status(status)
                .message(message)
                .timestamp(Instant.now())
                .data(data)
                .build();
    }

    /**
     * Creates an {@code ApiResponse} with the specified status and message, without a data payload.
     * <p>
     * The timestamp is automatically set to the current instant. This method is typically used for
     * no-content responses (HTTP 204) where a payload is not expected but a consistent structure is required.
     * The data field is {@code null} for these responses; since {@code @JsonInclude(NON_NULL)}
     * is applied, the {@code data} field is omitted from the serialized JSON output.
     *
     * @param <T> the type of the response payload
     * @param status the HTTP status code (typically 204)
     * @param message the human-readable response message
     * @return a new {@code ApiResponse} instance with no data payload
     * @see #noContent()
     */
    public static <T> ApiResponse<T> empty(int status, String message) {
        return new ApiResponse.Builder<T>()
                .status(status)
                .message(message)
                .timestamp(Instant.now())
                .build();
    }

    /**
     * Creates a successful {@code ApiResponse} with HTTP status 200 OK.
     * <p>
     * This is a convenience method that uses the default message "Request successful"
     * and automatically sets the timestamp to the current instant.
     *
     * @param <T> the type of the response payload
     * @param data the response payload
     * @return a new {@code ApiResponse} with status 200 and default success message
     * @see #of(int, String, Object)
     */
    public static <T> ApiResponse<T> ok(T data) {
        return of(200, "Request successful", data);
    }

    /**
     * Creates a successful {@code ApiResponse} with HTTP status 201 Created.
     * <p>
     * This is a convenience method for resource creation endpoints. It uses the
     * default message "Request created successfully" and automatically sets the
     * timestamp to the current instant.
     *
     * @param <T> the type of the response payload (typically the created resource)
     * @param data the response payload, usually the newly created resource
     * @return a new {@code ApiResponse} with status 201 and default creation message
     * @see #of(int, String, Object)
     */
    public static <T> ApiResponse<T> created(T data) {
        return of(201, "Request created successfully", data);
    }

    /**
     * Creates an {@code ApiResponse} with HTTP status 204 No Content.
     * <p>
     * This is a convenience method for successful requests that do not return a payload
     * (e.g., DELETE operations). The data field is {@code null} and will be omitted from
     * the JSON response; the message is "No content".
     *
     * @param <T> the type of the response payload (generic to maintain API consistency)
     * @return a new {@code ApiResponse} with status 204, empty data, and default no-content message
     * @see #empty(int, String)
     */
    public static <T> ApiResponse<T> noContent() {
        return empty(204, "No content");
    }
}
