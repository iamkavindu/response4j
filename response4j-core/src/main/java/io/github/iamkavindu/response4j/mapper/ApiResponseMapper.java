package io.github.iamkavindu.response4j.mapper;

import io.github.iamkavindu.response4j.annotation.SuccessResponse;
import io.github.iamkavindu.response4j.model.ApiResponse;


public class ApiResponseMapper {

    public <T>ApiResponse<?> map(T data, SuccessResponse annotation) {
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
