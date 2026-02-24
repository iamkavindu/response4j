package io.github.iamkavindu.response4j.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an exception class for RFC 7807 Problem Details mapping.
 * <p>
 * When an annotated exception is thrown, it is mapped to a {@link io.github.iamkavindu.response4j.model.ProblemDetail}
 * using the values specified here. Blank values fall back to defaults (e.g. exception class name for title).
 *
 * @see io.github.iamkavindu.response4j.model.ProblemDetail
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
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
     * URI reference identifying the problem type (RFC 7807).
     *
     * @return the type URI
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
