package io.github.response4j.core.annotation;

import io.github.response4j.core.model.ProblemDetail;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an exception class for RFC 9457 Problem Details mapping.
 * <p>
 * When an annotated exception is thrown, it is mapped to a {@link ProblemDetail}
 * using the values specified here. Blank values fall back to defaults (e.g. exception class name for title).
 *
 * @see ProblemDetail
 * @see <a href="https://www.rfc-editor.org/rfc/rfc9457">RFC 9457: Problem Details for HTTP APIs</a>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ProblemResponse {
    /**
     * HTTP status code for the problem response.
     *
     * @return the status code
     */
    int status() default 500;

    /**
     * Short, human-readable summary of the problem. If blank, the exception class simple name is used.
     *
     * @return the title
     */
    String title() default "";

    /**
     * URI reference identifying the problem type (RFC 9457).
     * <p>
     * Per RFC 9457 Section 4.2.1, when this is {@code "about:blank"}, the title MUST be
     * the HTTP reason phrase for the status code (e.g., "Not Found" for 404).
     *
     * @return the type URI
     * @see <a href="https://www.rfc-editor.org/rfc/rfc9457#section-4.2.1">RFC 9457 Section 4.2.1</a>
     */
    String type() default "about:blank";

    /**
     * Human-readable explanation of the problem. If blank and {@link #includeExceptionMessage()} is
     * true, the exception message is used.
     *
     * @return the detail text
     */
    String detail() default "";

    /**
     * Whether to use the exception message as the detail when {@link #detail()} is blank.
     *
     * @return true to include exception message when detail is blank
     */
    boolean includeExceptionMessage() default true;
}
