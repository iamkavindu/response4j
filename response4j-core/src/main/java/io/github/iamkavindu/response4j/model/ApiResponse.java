package io.github.iamkavindu.response4j.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;

/**
 * Generic success response wrapper with HTTP status, message, timestamp, and optional data.
 * <p>
 * Null fields are excluded from JSON serialization. The timestamp is formatted in ISO-8601 UTC
 * (e.g. {@code 2024-01-15T10:30:00Z}).
 *
 * @param <T> the type of the response data
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    /** HTTP status code. */
    private int status;

    /** Human-readable message describing the result. */
    private String message;

    /** UTC timestamp of the response in ISO-8601 format. */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant timestamp;

    /** Optional response payload; may be null. */
    private T data;

    /**
     * Creates an API response with the given status, message, and data.
     *
     * @param status  the HTTP status code
     * @param message the human-readable message
     * @param data    the response payload (may be null)
     * @param <T>     the type of the data
     * @return a new {@code ApiResponse} instance
     */
    public static <T> ApiResponse<T> of(int status, String message, T data) {
        return ApiResponse.<T>builder()
                .status(status)
                .message(message)
                .timestamp(Instant.now())
                .data(data)
                .build();
    }

    /**
     * Creates an API response with the given status and message, using an empty map as data.
     *
     * @param status  the HTTP status code
     * @param message the human-readable message
     * @param <T>     the type parameter (data will be an empty map)
     * @return a new {@code ApiResponse} instance
     */
    @SuppressWarnings("unchecked")
    public static <T> ApiResponse<T> empty(int status, String message) {
        return ApiResponse.<T>builder()
                .status(status)
                .message(message)
                .timestamp(Instant.now())
                .data((T) Map.of())
                .build();
    }

    /**
     * Creates a 200 OK response with the given data.
     *
     * @param data  the response payload (may be null)
     * @param <T>   the type of the data
     * @return a new {@code ApiResponse} with status 200 and message "Request successful"
     */
    public static <T> ApiResponse<T> ok(T data) {
        return of(200, "Request successful", data);
    }

    /**
     * Creates a 201 Created response with the given data.
     *
     * @param data  the response payload (may be null)
     * @param <T>   the type of the data
     * @return a new {@code ApiResponse} with status 201 and message "Request created successful"
     */
    public static <T> ApiResponse<T> created(T data) {
        return of(201, "Request created successful", data);
    }

    /**
     * Creates a 204 No Content response with empty data.
     *
     * @param <T> the type parameter (data will be an empty map)
     * @return a new {@code ApiResponse} with status 204 and message "No content"
     */
    public static <T> ApiResponse<T> noContent() {
        return empty(204, "No content");
    }
}
