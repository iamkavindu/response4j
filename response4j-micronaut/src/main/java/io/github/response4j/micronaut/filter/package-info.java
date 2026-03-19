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
 * <p>When {@code wrap = false} is set on the annotation, the body is passed through unchanged.
 * The filter operates in the reactive pipeline using Micronaut's {@code Publisher}-based filter
 * chain.
 *
 * <p>The filter is guarded by {@code @Requires(classes = {BasicHttpAttributes.class})}, which
 * means it is only registered when {@code io.micronaut.http.BasicHttpAttributes} is present on
 * the classpath. That class was introduced in Micronaut 4.8.0; on earlier Micronaut 4.x versions
 * the filter bean is silently skipped and success-response wrapping will not occur.
 *
 * @see io.github.response4j.core.annotation.SuccessResponse
 * @see io.github.response4j.core.model.ApiResponse
 * @see io.github.response4j.micronaut.factory
 * @since 0.1.0
 */
package io.github.response4j.micronaut.filter;
