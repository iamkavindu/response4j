/**
 * Framework-agnostic mappers that convert controller data and exceptions into response4j model types.
 *
 * <p>This package contains two mapper classes intended to be instantiated by framework integration
 * modules (Spring Boot, Quarkus, Micronaut) and shared as singletons:
 *
 * <ul>
 *   <li>{@link io.github.response4j.core.mapper.ApiResponseMapper} — Maps a raw controller return
 *       value to an {@link io.github.response4j.core.model.ApiResponse} envelope. When the
 *       controller method or class is annotated with
 *       {@link io.github.response4j.core.annotation.SuccessResponse}, the mapper uses the
 *       annotation's status code and message; otherwise it applies sensible defaults (200 OK for
 *       non-null data, 204 No Content for {@code null}). Setting {@code wrap = false} on the
 *       annotation causes the mapper to return {@code null}, signalling that the original body
 *       should be passed through unchanged.
 *   <li>{@link io.github.response4j.core.mapper.ProblemDetailMapper} — Maps a {@link Throwable}
 *       to a {@link io.github.response4j.core.model.ProblemDetail} conforming to
 *       <a href="https://www.rfc-editor.org/rfc/rfc9457">RFC 9457</a>. When the exception class
 *       is annotated with {@link io.github.response4j.core.annotation.ProblemResponse}, the mapper
 *       uses the declared status, title, type URI, and detail text, falling back to the exception's
 *       simple class name or message where annotation attributes are blank. Static extension members
 *       declared with {@link io.github.response4j.core.annotation.ProblemExtension} are extracted
 *       and included in the resulting problem detail. Unannotated exceptions produce a generic
 *       500 Internal Server Error problem detail.
 * </ul>
 *
 * <p>Neither mapper depends on any web framework; they operate purely on Java reflection and the
 * types in {@link io.github.response4j.core.model}. Framework integration modules are responsible
 * for providing the request URI as the RFC 9457 {@code instance} field when calling
 * {@link io.github.response4j.core.mapper.ProblemDetailMapper#map(Throwable, String)}.
 *
 * @see io.github.response4j.core.annotation
 * @see io.github.response4j.core.model
 * @since 0.1.0
 */
package io.github.response4j.core.mapper;
