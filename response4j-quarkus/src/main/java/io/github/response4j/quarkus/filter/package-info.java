/**
 * JAX-RS {@code ContainerResponseFilter} integration for automatic success-response wrapping in Quarkus.
 *
 * <p>{@link io.github.response4j.quarkus.filter.Response4jContainerResponseFilter} implements the
 * JAX-RS {@code ContainerResponseFilter} interface to intercept outbound responses after a
 * controller method has executed. When the matched resource method or its declaring class is
 * annotated with {@link io.github.response4j.core.annotation.SuccessResponse}, the filter
 * delegates to {@link io.github.response4j.core.mapper.ApiResponseMapper} to replace the entity
 * with an {@link io.github.response4j.core.model.ApiResponse} envelope before serialization.
 *
 * <p>When {@code wrap = false} is set on the annotation, the body is passed through unchanged.
 * The filter inspects the {@code ResourceInfo} provided by JAX-RS to locate the annotation on
 * the method first, then falls back to the class level.
 *
 * @see io.github.response4j.core.annotation.SuccessResponse
 * @see io.github.response4j.core.model.ApiResponse
 * @see io.github.response4j.quarkus.producer
 * @since 0.1.0
 */
package io.github.response4j.quarkus.filter;
