/**
 * Micronaut {@code @Factory} that provides response4j mapper beans for dependency injection.
 *
 * <p>{@link io.github.response4j.micronaut.factory.Response4jFactory} is a Micronaut
 * {@code @Factory} class that instantiates and exposes the two core mapper beans via
 * {@code @Bean @Primary} producer methods:
 *
 * <ul>
 *   <li>{@link io.github.response4j.core.mapper.ApiResponseMapper} — consumed by
 *       {@link io.github.response4j.micronaut.filter.Response4jHttpServerFilter} to wrap
 *       controller responses annotated with
 *       {@link io.github.response4j.core.annotation.SuccessResponse}.
 *   <li>{@link io.github.response4j.core.mapper.ProblemDetailMapper} — consumed by
 *       {@link io.github.response4j.micronaut.handler.Response4jExceptionHandler} to convert
 *       exceptions into RFC 9457 problem detail responses.
 * </ul>
 *
 * <p>The factory and its produced beans are discovered automatically by Micronaut's compile-time
 * dependency injection; no manual registration is required when the
 * {@code response4j-micronaut} dependency is on the classpath.
 *
 * @see io.github.response4j.micronaut.filter
 * @see io.github.response4j.micronaut.handler
 * @since 0.1.0
 */
package io.github.response4j.micronaut.factory;
