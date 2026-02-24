package io.github.iamkavindu.response4j.mapper;

import io.github.iamkavindu.response4j.annotation.SuccessResponse;
import io.github.iamkavindu.response4j.model.ApiResponse;

/**
 * Maps controller return values to {@link ApiResponse}, using {@link SuccessResponse} when present.
 * <p>
 * When the annotation specifies {@code wrap=false}, returns {@code null} (caller should pass through
 * the original body). When annotation is null, defaults to 200 OK with the provided data.
 */
public class ApiResponseMapper {

    /**
     * Maps the given data to an {@code ApiResponse}.
     * <p>
     * If {@code annotation} is non-null, its {@code status}, {@code message}, and {@code wrap}
     * values are used. If {@code wrap} is false, returns {@code null}. If {@code annotation} is
     * null, defaults to {@link ApiResponse#ok(Object)} for non-null data or
     * {@link ApiResponse#noContent()} for null.
     *
     * @param data       the controller return value (may be null)
     * @param annotation the {@code @SuccessResponse} metadata, or null for defaults
     * @param <T>        the type of the data
     * @return the mapped {@code ApiResponse}, or null when wrap is false
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
            return ApiResponse.empty(annotation.status(), annotation.message());
        }
        return ApiResponse.of(annotation.status(), annotation.message(), data);
    }

    private <T> ApiResponse<?> mapWithDefaults(T data) {
        if (data == null) {
            return ApiResponse.noContent();
        }
        return ApiResponse.ok(data);
    }
}
