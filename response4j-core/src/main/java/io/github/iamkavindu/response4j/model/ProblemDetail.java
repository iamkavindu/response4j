package io.github.iamkavindu.response4j.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.net.URI;
import java.util.Map;

/**
 * RFC 7807 Problem Details for HTTP APIs representation.
 * <p>
 * This immutable record provides a standardized, machine-readable format for specifying
 * errors in HTTP API responses, following the specification defined in
 * <a href="https://www.rfc-editor.org/rfc/rfc7807">RFC 7807</a>.
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
 * to appear as top-level properties alongside the standard RFC 7807 fields.
 * <p>
 * This record is thread-safe and immutable.
 *
 * @param type URI reference that identifies the problem type (RFC 7807 "type" member). Should resolve to
 *             human-readable documentation. Use {@code about:blank} when no specific type URI is available.
 * @param title short, human-readable summary of the problem type (RFC 7807 "title" member). Should not change
 *              between occurrences of the same problem type, except for localization.
 * @param status HTTP status code for this occurrence of the problem (RFC 7807 "status" member)
 * @param detail human-readable explanation specific to this occurrence of the problem (RFC 7807 "detail" member);
 *               may provide additional context beyond the title
 * @param instance URI reference identifying the specific occurrence of the problem (RFC 7807 "instance" member);
 *                 may be {@code null} if not applicable
 * @param extensions optional map of problem-specific extension members; serialized as top-level JSON properties
 *                   via {@link JsonAnyGetter}; may be {@code null}
 * @see io.github.iamkavindu.response4j.annotation.ProblemResponse
 * @see io.github.iamkavindu.response4j.mapper.ProblemDetailMapper
 * @see Builder
 * @see <a href="https://www.rfc-editor.org/rfc/rfc7807">RFC 7807: Problem Details for HTTP APIs</a>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProblemDetail(
        URI type,
        String title,
        int status,
        String detail,
        String instance,
        @JsonAnyGetter Map<String, Object> extensions
) {
    /**
     * Builder for constructing RFC 7807-compliant {@link ProblemDetail} instances using a fluent API.
     * <p>
     * This builder allows step-by-step construction of a {@code ProblemDetail} with explicit
     * control over all RFC 7807 standard and extension fields. For common use cases with sensible
     * defaults, prefer the {@link ProblemDetail#of(String, int, String, String, Map)} factory method.
     * <p>
     * The builder is typically used by the {@link io.github.iamkavindu.response4j.mapper.ProblemDetailMapper}
     * when mapping exceptions to problem details.
     *
     * @see ProblemDetail
     * @see <a href="https://www.rfc-editor.org/rfc/rfc7807">RFC 7807: Problem Details for HTTP APIs</a>
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
         * Per RFC 7807, this should be a URI reference that identifies the problem type and
         * ideally resolves to human-readable documentation. Use {@code URI.create("about:blank")}
         * when no specific documentation is available.
         *
         * @param type the problem type URI
         * @return this builder for method chaining
         */
        public Builder type(URI type) {
            this.type = type;
            return this;
        }

        /**
         * Sets the problem title.
         * <p>
         * Per RFC 7807, this should be a short, human-readable summary of the problem type.
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
         * Per RFC 7807, this is the HTTP status code generated by the origin server for
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
         * Per RFC 7807, this should be a human-readable explanation specific to this
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
         * Per RFC 7807, this is a URI reference that identifies the specific occurrence
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
         * Per RFC 7807, producers may add additional members to convey problem-specific
         * information. These extensions will be serialized as top-level JSON properties
         * alongside the standard RFC 7807 fields.
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
         * @return a new RFC 7807-compliant {@code ProblemDetail} instance
         */
        public ProblemDetail build() {
            return new ProblemDetail(type, title, status, detail, instance, extensions);
        }
    }

    /**
     * Creates a {@code ProblemDetail} with the specified fields and a default type URI.
     * <p>
     * This factory method constructs a problem detail with the problem type set to
     * {@code about:blank} per RFC 7807 conventions (indicating no specific problem type
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
     * @return a new RFC 7807-compliant {@code ProblemDetail} instance with type {@code about:blank}
     * @see Builder
     * @see <a href="https://www.rfc-editor.org/rfc/rfc7807">RFC 7807: Problem Details for HTTP APIs</a>
     */
    public static ProblemDetail of(String title, int status, String detail, String instance, Map<String, Object> extensions) {
        return new ProblemDetail.Builder()
                .type(URI.create("about:blank"))
                .title(title)
                .status(status)
                .detail(detail)
                .instance(instance)
                .extensions(extensions)
                .build();
    }
}
