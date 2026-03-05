package io.github.iamkavindu.response4j.model;

/**
 * Represents a single sub-error within a multi-problem response,
 * intended to be carried as an extension member in {@link ProblemDetail}.
 * <p>
 * Use this in the {@code extensions} map under the key {@code "errors"} to
 * communicate multiple validation or field-level failures in a single response,
 * following the pattern recommended by RFC 9457 Section 3.
 * <p>
 * RFC 9457 Section 3 provides guidance that a single Problem Detail response represents
 * a single logical problem. For cases involving multiple problems (e.g., bean validation
 * failures across several fields), the spec recommends carrying aggregated sub-errors via
 * extension members like this record.
 * <p>
 * Example usage:
 * <pre>{@code
 * List<ProblemDetailError> errors = List.of(
 *     new ProblemDetailError("/email", "must be a valid email address"),
 *     new ProblemDetailError("/age", "must be at least 18")
 * );
 * ProblemDetail problem = ProblemDetail.ofErrors(
 *     "Validation Failed",
 *     400,
 *     "The request contains invalid fields",
 *     errors
 * );
 * }</pre>
 *
 * @param pointer a JSON Pointer (RFC 6901) or field name identifying the source of the error
 * @param detail  human-readable explanation of this specific error
 * @see ProblemDetail#ofErrors(String, int, String, java.util.List)
 * @see <a href="https://www.rfc-editor.org/rfc/rfc9457#section-3">RFC 9457 Section 3</a>
 * @see <a href="https://www.rfc-editor.org/rfc/rfc6901">RFC 6901: JSON Pointer</a>
 */
public record ProblemDetailError(String pointer, String detail) {}
