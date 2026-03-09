/**
 * Micronaut HTTP server filter integration for automatic success-response wrapping.
 *
 * <p>{@link io.github.response4j.micronaut.filter.Response4jHttpServerFilter} implements
 * Micronaut's {@code HttpServerFilter} interface to intercept outbound responses after a
 * controller method has executed. When the matched route handler method or its declaring class is
 * annotated with {@link io.github.response4j.core.annotation.SuccessResponse}, the filter
 * delegates to {@link io.github.response4j.core.mapper.ApiResponseMapper} to wrap the response
 * body in an {@link io.github.response4j.core.model.ApiResponse} envelope before it is written to
 * the client.
 *
 * <p>When {@code wrap = false} is set on the annotation, or when the body is already an
 * {@code ApiResponse}, the response is passed through unchanged. The filter operates in the
 * reactive pipeline using Micronaut's {@code Publisher}-based filter chain.
 *
 * @see io.github.response4j.core.annotation.SuccessResponse
 * @see io.github.response4j.core.model.ApiResponse
 * @see io.github.response4j.micronaut.factory
 * @since 0.1.0
 */
package io.github.response4j.micronaut.filter;
