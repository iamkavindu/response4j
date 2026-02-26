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

@Provider
@ApplicationScoped
public class Response4jContainerResponseFilter implements ContainerResponseFilter {

    private final ApiResponseMapper apiResponseMapper;

    @Context
    ResourceInfo resourceInfo;

    @Inject
    public Response4jContainerResponseFilter(ApiResponseMapper apiResponseMapper) {
        this.apiResponseMapper = apiResponseMapper;
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {

        Method resourceMethod = resourceInfo.getResourceMethod();
        if (resourceMethod == null) return;

        if (responseContext.getEntity() instanceof ApiResponse<?>) return;

        SuccessResponse annotation = resourceMethod.getAnnotation(SuccessResponse.class);
        if (annotation == null) {
            annotation = resourceInfo.getResourceClass()
                    .getAnnotation(SuccessResponse.class);
        }

        if (annotation == null) return;

        Object body = responseContext.getEntity();
        ApiResponse<?> apiResponse = apiResponseMapper.map(body, annotation);

        if (apiResponse != null) {
            responseContext.setStatus(apiResponse.getStatus());
            responseContext.setEntity(apiResponse);
        }
    }
}
