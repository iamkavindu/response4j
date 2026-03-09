/**
 * Immutable model types for standardized API success and error responses.
 *
 * <p>This package contains four types that form the core data model of response4j:
 *
 * <ul>
 *   <li>{@link io.github.response4j.core.model.ApiResponse} — An immutable record that wraps a
 *       successful HTTP response payload in a consistent envelope containing an HTTP status code,
 *       a human-readable message, a UTC timestamp, and the typed payload. Static factory methods
 *       ({@code ok}, {@code created}, {@code noContent}) cover the most common HTTP success codes;
 *       a fluent {@link io.github.response4j.core.model.ApiResponse.Builder Builder} is available
 *       for custom cases.
 *   <li>{@link io.github.response4j.core.model.ProblemDetail} — An immutable record that
 *       represents an HTTP error response conforming to
 *       <a href="https://www.rfc-editor.org/rfc/rfc9457">RFC 9457 Problem Details for HTTP APIs</a>.
 *       It carries the standard RFC 9457 members ({@code type}, {@code title}, {@code status},
 *       {@code detail}, {@code instance}) together with an optional extensions map that is
 *       flattened into the JSON output as top-level properties. Use {@code ProblemDetail.of()} for
 *       single-problem responses or {@code ProblemDetail.ofErrors()} to aggregate multiple
 *       validation sub-errors.
 *   <li>{@link io.github.response4j.core.model.ProblemDetailError} — An immutable record
 *       representing a single field-level error within a multi-error problem response. It carries
 *       a JSON Pointer ({@code pointer}) identifying the invalid field and a human-readable
 *       {@code detail} message. A list of these is embedded in the {@code "errors"} extension
 *       member of a {@code ProblemDetail} via {@code ProblemDetail.ofErrors()}.
 *   <li>{@link io.github.response4j.core.model.ProblemTypes} — A utility class holding
 *       {@code URI} constants for well-known problem type URIs, including {@code about:blank} and
 *       the IANA HTTP Problem Types registry base URI. Using these constants avoids raw string
 *       literals and ensures RFC 9457 compliance.
 * </ul>
 *
 * <p>All records in this package are thread-safe and immutable. JSON serialization is handled
 * by Jackson; {@code null} fields are excluded from the output by default.
 *
 * @see io.github.response4j.core.annotation
 * @see io.github.response4j.core.mapper
 * @see <a href="https://www.rfc-editor.org/rfc/rfc9457">RFC 9457: Problem Details for HTTP APIs</a>
 * @since 0.1.0
 */
package io.github.response4j.core.model;
