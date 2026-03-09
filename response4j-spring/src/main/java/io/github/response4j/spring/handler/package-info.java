/**
 * Spring {@code @ControllerAdvice} integration for global RFC 9457 exception handling.
 *
 * <p>{@link io.github.response4j.spring.handler.Response4jExceptionHandler} is a Spring
 * {@code @ControllerAdvice} that catches {@link java.lang.Exception} globally and converts it to
 * an {@link io.github.response4j.core.model.ProblemDetail} HTTP response using
 * {@link io.github.response4j.core.mapper.ProblemDetailMapper}. The request URI is extracted from
 * the {@code HttpServletRequest} and set as the RFC 9457 {@code instance} field to identify the
 * specific occurrence of the problem.
 *
 * <p>When the thrown exception is annotated with
 * {@link io.github.response4j.core.annotation.ProblemResponse}, the handler uses the status code,
 * title, type URI, and detail declared on that annotation. Unannotated exceptions produce a generic
 * 500 Internal Server Error problem detail. The response {@code Content-Type} is set to
 * {@code application/problem+json} as required by RFC 9457.
 *
 * <p>The handler is registered automatically by
 * {@link io.github.response4j.spring.autoconfigure.Response4jAutoConfiguration} and requires no
 * additional configuration.
 *
 * @see io.github.response4j.core.annotation.ProblemResponse
 * @see io.github.response4j.core.model.ProblemDetail
 * @see io.github.response4j.spring.autoconfigure
 * @see <a href="https://www.rfc-editor.org/rfc/rfc9457">RFC 9457: Problem Details for HTTP APIs</a>
 * @since 0.1.0
 */
package io.github.response4j.spring.handler;
