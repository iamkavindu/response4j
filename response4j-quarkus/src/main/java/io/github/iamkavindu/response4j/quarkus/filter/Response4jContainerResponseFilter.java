package io.github.iamkavindu.response4j.quarkus.filter;

import io.github.iamkavindu.response4j.annotation.SuccessResponse;
import io.github.iamkavindu.response4j.mapper.ApiResponseMapper;
import io.github.iamkavindu.response4j.model.ApiResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * JAX-RS {@link ContainerResponseFilter} that wraps resource method responses in
 * {@link ApiResponse} when the method or declaring class is annotated with {@link SuccessResponse}.
 * <p>
 * Passes through existing {@code ApiResponse} bodies unchanged. When {@code SuccessResponse}
 * is absent or the resource method is unknown, leaves the response unchanged.
 */
@Provider
@ApplicationScoped
public class Response4jContainerResponseFilter implements ContainerResponseFilter {

    private final ApiResponseMapper apiResponseMapper;

    @Context
    ResourceInfo resourceInfo;

    /**
     * Creates the filter with the given {@link ApiResponseMapper}.
     *
     * @param apiResponseMapper the mapper for converting controller return values to ApiResponse
     */
    @Inject
    public Response4jContainerResponseFilter(ApiResponseMapper apiResponseMapper) {
        this.apiResponseMapper = apiResponseMapper;
    }

    /**
     * Wraps the response body in {@link ApiResponse} when the resource method or class has
     * {@link SuccessResponse}. Updates the response status from the mapped ApiResponse.
     *
     * @param requestContext  the request context
     * @param responseContext the response context (may be modified)
     */
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {

        Method resourceMethod = resourceInfo.getResourceMethod();
        if (resourceMethod == null) return;

        if (responseContext.getEntity() instanceof ApiResponse<?>) return;

        SuccessResponse annotation = resourceMethod.getAnnotation(SuccessResponse.class);
        if (annotation == null) {
            annotation = resourceInfo.getResourceClass().getAnnotation(SuccessResponse.class);
        }

        if (annotation == null) return;

        Object body = responseContext.getEntity();
        ApiResponse<?> apiResponse = apiResponseMapper.map(body, annotation);

        if (apiResponse != null) {
            responseContext.setStatus(apiResponse.status());
            responseContext.setEntity(apiResponse);
        }
    }
}
