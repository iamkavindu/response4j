package io.github.iamkavindu.response4j.spring.advice;

import io.github.iamkavindu.response4j.annotation.SuccessResponse;
import io.github.iamkavindu.response4j.mapper.ApiResponseMapper;
import io.github.iamkavindu.response4j.model.ApiResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class Response4jResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private final ApiResponseMapper apiResponseMapper;

    public Response4jResponseBodyAdvice(ApiResponseMapper apiResponseMapper) {
        this.apiResponseMapper = apiResponseMapper;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.hasMethodAnnotation(SuccessResponse.class)
                || returnType.getDeclaringClass().isAnnotationPresent(SuccessResponse.class);
    }

    @Nullable
    @Override
    public Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        if (body instanceof ApiResponse<?> apiResponseBody) {
            if (apiResponseBody.getStatus() == 204) {
                response.setStatusCode(HttpStatusCode.valueOf(204));
                return null;
            }
            return body;
        }

        SuccessResponse annotation = returnType.getMethodAnnotation(SuccessResponse.class);
        if (annotation == null) {
            annotation = returnType.getDeclaringClass().getAnnotation(SuccessResponse.class);
        }
        ApiResponse<?> apiResponse = apiResponseMapper.map(body, annotation);

        if (apiResponse != null) {
            response.setStatusCode(
                    HttpStatusCode.valueOf(apiResponse.getStatus())
            );
            if (apiResponse.getStatus() == 204) {
                return null;
            }
        }

        return apiResponse != null ? apiResponse : body;
    }
}
