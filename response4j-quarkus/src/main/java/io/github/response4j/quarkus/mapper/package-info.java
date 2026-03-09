/**
 * JAX-RS {@code ExceptionMapper} integration for global RFC 9457 exception handling in Quarkus.
 *
 * <p>{@link io.github.response4j.quarkus.mapper.Response4jExceptionMapper} implements the JAX-RS
 * {@code ExceptionMapper<Exception>} interface to catch all unhandled exceptions and convert them
 * to {@link io.github.response4j.core.model.ProblemDetail} JSON responses. It delegates to
 * {@link io.github.response4j.core.mapper.ProblemDetailMapper} for the actual mapping logic and
 * extracts the request URI from the injected {@code UriInfo} to populate the RFC 9457
 * {@code instance} field.
 *
 * <p>When the thrown exception is annotated with
 * {@link io.github.response4j.core.annotation.ProblemResponse}, the mapper applies the declared
 * status, title, type URI, and detail. Unannotated exceptions fall back to a generic 500 Internal
 * Server Error problem detail. The JAX-RS {@code Response} is produced with
 * {@code Content-Type: application/problem+json} and the HTTP status from the
 * {@code ProblemDetail}.
 *
 * @see io.github.response4j.core.annotation.ProblemResponse
 * @see io.github.response4j.core.model.ProblemDetail
 * @see io.github.response4j.quarkus.producer
 * @see <a href="https://www.rfc-editor.org/rfc/rfc9457">RFC 9457: Problem Details for HTTP APIs</a>
 * @since 0.1.0
 */
package io.github.response4j.quarkus.mapper;
