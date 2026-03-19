package io.github.response4j.core.mapper;

import java.time.Instant;

import io.github.response4j.core.annotation.SuccessResponse;
import io.github.response4j.core.model.ApiResponse;

/**
 * Maps controller response data to {@link ApiResponse} instances based on {@link SuccessResponse} annotations.
 * <p>
 * This mapper handles the transformation of raw controller return values into standardized
 * {@code ApiResponse} wrappers. It respects the {@code wrap} attribute of the annotation and
 * provides sensible defaults when no annotation is present.
 * <p>
 * Used by framework integration modules (Spring, Quarkus, Micronaut) to automatically wrap
 * controller responses in consistent API response structures.
 *
 * @see ApiResponse
 * @see SuccessResponse
 */
public class ApiResponseMapper {

    /**
     * Maps the given data to an {@code ApiResponse} based on the provided annotation.
     * <p>
     * When an annotation is present, uses its status, message, and wrap settings. When
     * {@code wrap=false}, returns {@code null} to signal that the original data should be
     * returned unwrapped. For {@code null} data or status 204, creates an empty response.
     * <p>
     * When no annotation is present, applies default behavior: {@code null} data produces
     * a 204 No Content response, and non-null data produces a 200 OK response.
     *
     * @param <T> the type of the response data
     * @param data the controller return value to wrap; may be {@code null}
     * @param annotation the {@code @SuccessResponse} annotation from the controller method or class;
     *                   may be {@code null} to use defaults
     * @return an {@code ApiResponse} wrapping the data, or {@code null} if {@code wrap=false}
     */
    public <T> ApiResponse<?> map(T data, SuccessResponse annotation) {
        if (annotation != null) {
            return mapFromAnnotation(data, annotation);
        }
        return mapWithDefaults(data);
    }

    private <T> ApiResponse<?> mapFromAnnotation(T data, SuccessResponse annotation) {
        if (!annotation.wrap()) {
            return null;
        }
        if (data == null || annotation.status() == 204) {
            return new ApiResponse<>(annotation.status(), annotation.message(), Instant.now(), null);
        }
        return new ApiResponse<>(annotation.status(), annotation.message(), Instant.now(), data);
    }

    private <T> ApiResponse<?> mapWithDefaults(T data) {
        if (data == null) {
            return new ApiResponse<>(204, "No content", Instant.now(), null);
        }
        return new ApiResponse<>(200, "Request successful", Instant.now(), data);
    }
}
