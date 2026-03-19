/**
 * CDI producer that exposes the response4j mapper bean in a Quarkus application.
 *
 * <p>{@link io.github.response4j.quarkus.producer.Response4jProducer} is a CDI
 * {@code @ApplicationScoped} producer class that creates and exposes the core mapper
 * singleton as an injectable CDI bean:
 *
 * <ul>
 *   <li>{@link io.github.response4j.core.mapper.ProblemDetailMapper} — consumed by
 *       {@link io.github.response4j.quarkus.mapper.Response4jExceptionMapper} to convert
 *       exceptions into RFC 9457 problem detail responses.
 * </ul>
 *
 * <p>The bean is discovered automatically via Quarkus CDI bean discovery; no manual
 * registration is required when the {@code response4j-quarkus} extension is on the classpath.
 *
 * @see io.github.response4j.quarkus.mapper
 * @since 0.1.0
 */
package io.github.response4j.quarkus.producer;
