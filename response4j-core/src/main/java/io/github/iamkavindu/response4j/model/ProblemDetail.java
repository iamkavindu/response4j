package io.github.iamkavindu.response4j.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * RFC 9457 Problem Details for HTTP APIs representation.
 * <p>
 * This immutable record provides a standardized, machine-readable format for specifying
 * errors in HTTP API responses, following the specification defined in
 * <a href="https://www.rfc-editor.org/rfc/rfc9457">RFC 9457</a>.
 * <p>
 * Problem details include a problem type URI, title, status code, human-readable detail,
 * optional instance identifier, and extensible additional properties. This record is
 * framework-agnostic and can be used with Spring Boot, Quarkus, Micronaut, or standalone.
 * <p>
 * Instances are typically created automatically by exception handlers when an exception
 * annotated with {@link io.github.iamkavindu.response4j.annotation.ProblemResponse} is thrown,
 * or manually constructed via the {@link Builder} or {@link #of(String, int, String, String, Map)}
 * factory method. The {@link io.github.iamkavindu.response4j.mapper.ProblemDetailMapper} handles
 * the mapping from exceptions to problem details.
 * <p>
 * JSON serialization is handled by Jackson annotations. The {@code extensions} map is flattened
 * into the JSON output using {@link JsonAnyGetter}, allowing problem-specific extension members
 * to appear as top-level properties alongside the standard RFC 9457 fields.
 * <p>
 * This record is thread-safe and immutable.
 *
 * @param type URI reference that identifies the problem type (RFC 9457 "type" member). Should resolve to
 *             human-readable documentation. Use {@code about:blank} when no specific type URI is available.
 *             Per RFC 9457 Section 4.2.1, when type is {@code about:blank}, the title MUST be the HTTP reason phrase.
 * @param title short, human-readable summary of the problem type (RFC 9457 "title" member). Should not change
 *              between occurrences of the same problem type, except for localization.
 * @param status HTTP status code for this occurrence of the problem (RFC 9457 "status" member)
 * @param detail human-readable explanation specific to this occurrence of the problem (RFC 9457 "detail" member);
 *               may provide additional context beyond the title
 * @param instance URI reference identifying the specific occurrence of the problem (RFC 9457 "instance" member);
 *                 may be {@code null} if not applicable
 * @param extensions optional map of problem-specific extension members; serialized as top-level JSON properties
 *                   via {@link JsonAnyGetter}; may be {@code null}
 * @see io.github.iamkavindu.response4j.annotation.ProblemResponse
 * @see io.github.iamkavindu.response4j.mapper.ProblemDetailMapper
 * @see Builder
 * @see <a href="https://www.rfc-editor.org/rfc/rfc9457">RFC 9457: Problem Details for HTTP APIs</a>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProblemDetail(
        URI type,
        String title,
        int status,
        String detail,
        String instance,
        @JsonAnyGetter Map<String, Object> extensions) {
    /**
     * Builder for constructing RFC 9457-compliant {@link ProblemDetail} instances using a fluent API.
     * <p>
     * This builder allows step-by-step construction of a {@code ProblemDetail} with explicit
     * control over all RFC 9457 standard and extension fields. For common use cases with sensible
     * defaults, prefer the {@link ProblemDetail#of(String, int, String, String, Map)} factory method.
     * <p>
     * The builder is typically used by the {@link io.github.iamkavindu.response4j.mapper.ProblemDetailMapper}
     * when mapping exceptions to problem details.
     *
     * @see ProblemDetail
     * @see <a href="https://www.rfc-editor.org/rfc/rfc9457">RFC 9457: Problem Details for HTTP APIs</a>
     */
    public static class Builder {
        private URI type;
        private String title;
        private int status;
        private String detail;
        private String instance;
        private Map<String, Object> extensions;

        /**
         * Sets the problem type URI.
         * <p>
         * Per RFC 9457, this should be a URI reference that identifies the problem type and
         * ideally resolves to human-readable documentation. Use {@code URI.create("about:blank")}
         * when no specific documentation is available. Per RFC 9457 Section 4.2.1, when type is
         * {@code about:blank}, the title MUST be the HTTP reason phrase for the status code.
         *
         * @param type the problem type URI
         * @return this builder for method chaining
         * @see <a href="https://www.rfc-editor.org/rfc/rfc9457#section-4.2.1">RFC 9457 Section 4.2.1</a>
         */
        public Builder type(URI type) {
            this.type = type;
            return this;
        }

        /**
         * Sets the problem title.
         * <p>
         * Per RFC 9457, this should be a short, human-readable summary of the problem type.
         * The title should remain consistent across different occurrences of the same problem type.
         *
         * @param title the problem title
         * @return this builder for method chaining
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * Sets the HTTP status code.
         * <p>
         * Per RFC 9457, this is the HTTP status code generated by the origin server for
         * this occurrence of the problem.
         *
         * @param status the HTTP status code (e.g., 400, 404, 500)
         * @return this builder for method chaining
         */
        public Builder status(int status) {
            this.status = status;
            return this;
        }

        /**
         * Sets the problem detail.
         * <p>
         * Per RFC 9457, this should be a human-readable explanation specific to this
         * occurrence of the problem, providing additional context beyond the title.
         *
         * @param detail the problem detail explanation
         * @return this builder for method chaining
         */
        public Builder detail(String detail) {
            this.detail = detail;
            return this;
        }

        /**
         * Sets the instance identifier.
         * <p>
         * Per RFC 9457, this is a URI reference that identifies the specific occurrence
         * of the problem. It may be used to reference the problem in logs or support tickets.
         *
         * @param instance the instance URI reference, or {@code null} if not applicable
         * @return this builder for method chaining
         */
        public Builder instance(String instance) {
            this.instance = instance;
            return this;
        }

        /**
         * Sets problem-specific extension members.
         * <p>
         * Per RFC 9457, producers may add additional members to convey problem-specific
         * information. These extensions will be serialized as top-level JSON properties
         * alongside the standard RFC 9457 fields.
         *
         * @param extensions map of extension field names to values, or {@code null} if no extensions
         * @return this builder for method chaining
         */
        public Builder extensions(Map<String, Object> extensions) {
            this.extensions = extensions;
            return this;
        }

        /**
         * Constructs an immutable {@link ProblemDetail} with the configured values.
         *
         * @return a new RFC 9457-compliant {@code ProblemDetail} instance
         */
        public ProblemDetail build() {
            return new ProblemDetail(type, title, status, detail, instance, extensions);
        }
    }

    /**
     * Creates a {@code ProblemDetail} with the specified fields and a default type URI.
     * <p>
     * This factory method constructs a problem detail with the problem type set to
     * {@code about:blank} per RFC 9457 conventions (indicating no specific problem type
     * documentation is available). For more control over the type URI, use the {@link Builder}.
     * <p>
     * This method is typically used for ad-hoc error responses or when the
     * {@link io.github.iamkavindu.response4j.mapper.ProblemDetailMapper} processes exceptions
     * without {@link io.github.iamkavindu.response4j.annotation.ProblemResponse} annotations.
     *
     * @param title short, human-readable summary of the problem type
     * @param status HTTP status code for this occurrence of the problem
     * @param detail human-readable explanation specific to this occurrence
     * @param instance URI reference identifying the specific occurrence, or {@code null}
     * @param extensions map of problem-specific extension members, or {@code null}
     * @return a new RFC 9457-compliant {@code ProblemDetail} instance with type {@code about:blank}
     * @see Builder
     * @see <a href="https://www.rfc-editor.org/rfc/rfc9457">RFC 9457: Problem Details for HTTP APIs</a>
     */
    public static ProblemDetail of(
            String title, int status, String detail, String instance, Map<String, Object> extensions) {
        return new ProblemDetail.Builder()
                .type(ProblemTypes.ABOUT_BLANK)
                .title(title)
                .status(status)
                .detail(detail)
                .instance(instance)
                .extensions(extensions)
                .build();
    }

    /**
     * Creates a {@code ProblemDetail} carrying multiple sub-errors in the {@code "errors"} extension field.
     * <p>
     * This factory method is suitable for validation failures or any scenario with multiple simultaneous
     * problems. The {@code errors} list is placed in the extensions map under the key {@code "errors"},
     * following the RFC 9457 Section 3 recommendation for multi-problem responses.
     * <p>
     * Per RFC 9457 Section 3, a single Problem Detail response represents a single logical problem.
     * For cases involving multiple problems (e.g., bean validation failures across several fields),
     * the spec recommends carrying aggregated sub-errors via extension members.
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
     * @param title  short summary of the problem type
     * @param status HTTP status code for this occurrence of the problem
     * @param detail overall explanation of the problem
     * @param errors list of field-level or sub-errors; may be empty but not {@code null}
     * @return a new {@code ProblemDetail} with the errors embedded as an extension under the key {@code "errors"}
     * @see ProblemDetailError
     * @see <a href="https://www.rfc-editor.org/rfc/rfc9457#section-3">RFC 9457 Section 3</a>
     */
    public static ProblemDetail ofErrors(String title, int status, String detail, List<ProblemDetailError> errors) {
        return new ProblemDetail.Builder()
                .type(ProblemTypes.ABOUT_BLANK)
                .title(title)
                .status(status)
                .detail(detail)
                .extensions(Map.of("errors", errors))
                .build();
    }
}
