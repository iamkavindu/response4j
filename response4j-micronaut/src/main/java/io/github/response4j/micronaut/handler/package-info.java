/**
 * Micronaut exception handler integration for global RFC 9457 exception handling.
 *
 * <p>{@link io.github.response4j.micronaut.handler.Response4jExceptionHandler} implements
 * Micronaut's {@code ExceptionHandler<Exception, HttpResponse<?>>} interface to catch all
 * unhandled exceptions and convert them to
 * {@link io.github.response4j.core.model.ProblemDetail} HTTP responses. It delegates to
 * {@link io.github.response4j.core.mapper.ProblemDetailMapper} for the mapping logic and
 * extracts the request URI from the {@code HttpRequest} to populate the RFC 9457 {@code instance}
 * field, identifying the specific occurrence of the problem.
 *
 * <p>When the thrown exception is annotated with
 * {@link io.github.response4j.core.annotation.ProblemResponse}, the handler applies the declared
 * status, title, type URI, and detail. Unannotated exceptions fall back to a generic 500 Internal
 * Server Error problem detail. The {@code HttpResponse} is produced with
 * {@code Content-Type: application/problem+json} and the HTTP status derived from the
 * {@code ProblemDetail}.
 *
 * @see io.github.response4j.core.annotation.ProblemResponse
 * @see io.github.response4j.core.model.ProblemDetail
 * @see io.github.response4j.micronaut.factory
 * @see <a href="https://www.rfc-editor.org/rfc/rfc9457">RFC 9457: Problem Details for HTTP APIs</a>
 * @since 0.1.0
 */
package io.github.response4j.micronaut.handler;
