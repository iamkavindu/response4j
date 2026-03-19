/**
 * Framework-agnostic mapper that converts exceptions into response4j model types.
 *
 * <p>This package contains one mapper class intended to be instantiated by framework integration
 * modules (Spring Boot, Quarkus, Micronaut) and shared as a singleton:
 *
 * <ul>
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
 * <p>The mapper does not depend on any web framework; it operates purely on Java reflection and
 * the types in {@link io.github.response4j.core.model}. Framework integration modules are
 * responsible for providing the request URI as the RFC 9457 {@code instance} field when calling
 * {@link io.github.response4j.core.mapper.ProblemDetailMapper#map(Throwable, String)}.
 *
 * @see io.github.response4j.core.annotation
 * @see io.github.response4j.core.model
 * @since 0.1.0
 */
package io.github.response4j.core.mapper;
