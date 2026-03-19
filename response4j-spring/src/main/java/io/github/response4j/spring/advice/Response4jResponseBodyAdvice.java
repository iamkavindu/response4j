package io.github.response4j.spring.advice;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import io.github.response4j.core.annotation.SuccessResponse;
import io.github.response4j.core.mapper.ApiResponseMapper;
import io.github.response4j.core.model.ApiResponse;

/**
 * Spring {@link ResponseBodyAdvice} that wraps controller responses in {@link ApiResponse} when
 * the method or declaring class is annotated with {@link SuccessResponse}.
 * <p>
 * Passes through existing {@code ApiResponse} bodies unchanged. For 204 No Content responses,
 * sets the status and returns null (no body). When {@code wrap} is false on the annotation,
 * returns the original body without wrapping.
 */
@RestControllerAdvice
public class Response4jResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private final ApiResponseMapper apiResponseMapper;

    /**
     * Creates the advice with the given mapper.
     *
     * @param apiResponseMapper the mapper for converting controller return values to ApiResponse
     */
    public Response4jResponseBodyAdvice(ApiResponseMapper apiResponseMapper) {
        this.apiResponseMapper = apiResponseMapper;
    }

    /**
     * Returns true when the method or declaring class has {@link SuccessResponse}.
     *
     * @param returnType    the controller method return type
     * @param converterType the selected message converter
     * @return true if advice applies
     */
    @Override
    public boolean supports(
            MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.hasMethodAnnotation(SuccessResponse.class)
                || returnType.getDeclaringClass().isAnnotationPresent(SuccessResponse.class);
    }

    /**
     * Wraps the body in {@link ApiResponse} when applicable, or returns the original body.
     * Sets the response status from the mapped ApiResponse. For 204, returns null so no body is written.
     *
     * @param body                      the controller return value (may be null)
     * @param returnType                the controller method return type
     * @param selectedContentType       the selected content type
     * @param selectedConverterType     the selected message converter
     * @param request                   the current request
     * @param response                  the current response
     * @return the body to write (possibly wrapped), or null for 204 No Content
     */
    @Nullable @Override
    public Object beforeBodyWrite(
            @Nullable Object body,
            @NonNull MethodParameter returnType,
            @NonNull MediaType selectedContentType,
            @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response) {

        SuccessResponse annotation = returnType.getMethodAnnotation(SuccessResponse.class);
        if (annotation == null) {
            annotation = returnType.getDeclaringClass().getAnnotation(SuccessResponse.class);
        }
        ApiResponse<?> apiResponse = apiResponseMapper.map(body, annotation);

        if (apiResponse != null) {
            response.setStatusCode(HttpStatusCode.valueOf(apiResponse.status()));
            if (apiResponse.status() == 204) {
                return null;
            }
        }

        return apiResponse != null ? apiResponse : body;
    }
}
