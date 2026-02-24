package io.github.iamkavindu.response4j.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.net.URI;
import java.util.Map;

/**
 * RFC 7807 Problem Details for HTTP APIs representation.
 * <p>
 * Used for standardized machine-readable error responses. See
 * <a href="https://www.rfc-editor.org/rfc/rfc7807">RFC 7807</a> for the specification.
 * Null fields are excluded from JSON serialization.
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProblemDetail {

    /** URI reference identifying the problem type. */
    private URI type;

    /** Short, human-readable summary of the problem. */
    private String title;

    /** HTTP status code applicable to the problem. */
    private int status;

    /** Human-readable explanation specific to this occurrence of the problem. */
    private String detail;

    /** URI reference identifying the specific occurrence of the problem. */
    private String instance;

    /** Additional problem-specific extensions. */
    @JsonAnyGetter
    private Map<String, Object> extensions;

    /**
     * Creates a problem detail with type {@code about:blank} and the given status, title, and detail.
     *
     * @param status the HTTP status code
     * @param title  short summary of the problem
     * @param detail human-readable explanation of the occurrence
     * @return a new {@code ProblemDetail} instance
     */
    public static ProblemDetail of(int status, String title, String detail) {
        return ProblemDetail.builder()
                .type(URI.create("about:blank"))
                .status(status)
                .title(title)
                .detail(detail)
                .build();
    }
}
