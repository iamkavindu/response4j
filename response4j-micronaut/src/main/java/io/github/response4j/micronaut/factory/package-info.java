/**
 * Micronaut {@code @Factory} that provides the response4j mapper bean for dependency injection.
 *
 * <p>{@link io.github.response4j.micronaut.factory.Response4jFactory} is a Micronaut
 * {@code @Factory} class that instantiates and exposes the core mapper bean via a
 * {@code @Bean @Primary} producer method:
 *
 * <ul>
 *   <li>{@link io.github.response4j.core.mapper.ProblemDetailMapper} — consumed by
 *       {@link io.github.response4j.micronaut.handler.Response4jExceptionHandler} to convert
 *       exceptions into RFC 9457 problem detail responses.
 * </ul>
 *
 * <p>The factory and its produced bean are discovered automatically by Micronaut's compile-time
 * dependency injection; no manual registration is required when the
 * {@code response4j-micronaut} dependency is on the classpath.
 *
 * @see io.github.response4j.micronaut.handler
 * @since 0.1.0
 */
package io.github.response4j.micronaut.factory;
