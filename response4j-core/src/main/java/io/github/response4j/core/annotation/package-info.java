/**
 * Core annotations that drive response4j behaviour at the exception level.
 *
 * <p>This package contains three annotation types:
 *
 * <ul>
 *   <li>{@link io.github.response4j.core.annotation.ProblemResponse} — Applied to an exception
 *       class to declare its RFC 9457 mapping. When an annotated exception is thrown, the
 *       framework integration converts it to a
 *       {@link io.github.response4j.core.model.ProblemDetail} using the status, title, type URI,
 *       and detail text declared on the annotation.
 *   <li>{@link io.github.response4j.core.annotation.ProblemExtension} — A repeatable annotation
 *       applied alongside {@code @ProblemResponse} to attach static key/value extension members
 *       to the resulting {@code ProblemDetail}. The container annotation
 *       {@link io.github.response4j.core.annotation.ProblemExtensions} is generated
 *       automatically by the Java compiler when multiple {@code @ProblemExtension} annotations
 *       are present on the same exception class.
 * </ul>
 *
 * <p>All annotations are retained at runtime ({@code @Retention(RUNTIME)}) so that framework
 * integration modules can read them via reflection during request processing.
 *
 * @see io.github.response4j.core.model
 * @see io.github.response4j.core.mapper
 * @see <a href="https://www.rfc-editor.org/rfc/rfc9457">RFC 9457: Problem Details for HTTP APIs</a>
 * @since 0.1.0
 */
package io.github.response4j.core.annotation;
