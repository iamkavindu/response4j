package io.github.response4j.core.model;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.github.response4j.core.annotation.SuccessResponse;

/**
 * Standardized envelope for successful API responses.
 * <p>
 * This immutable record provides a consistent structure for wrapping HTTP response data
 * across different Java web frameworks (Spring Boot, Quarkus, Micronaut). It includes
 * status code, message, timestamp, and optional payload.
 * <p>
 * Instances are created internally by the library when a controller method or class is
 * annotated with {@link SuccessResponse}. This class is not intended for direct instantiation
 * by host applications.
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
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        int status,
        String message,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        Instant timestamp,

        T data) {}
