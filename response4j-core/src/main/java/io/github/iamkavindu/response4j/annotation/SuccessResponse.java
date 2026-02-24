package io.github.iamkavindu.response4j.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a controller method or class for success response wrapping into {@link io.github.iamkavindu.response4j.model.ApiResponse}.
 * <p>
 * When applied, the framework will wrap the return value in a standardized API response structure.
 * Use a type-level annotation to apply defaults to all methods of a controller.
 *
 * @see io.github.iamkavindu.response4j.model.ApiResponse
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SuccessResponse {
    /**
     * HTTP status code for the response.
     *
     * @return the status code
     */
    int status() default 200;

    /**
     * Human-readable message included in the response.
     *
     * @return the message
     */
    String message() default "Request successful";

    /**
     * Whether to wrap the return value in {@code ApiResponse}. If {@code false}, the original
     * body is returned without wrapping.
     *
     * @return true to wrap, false to pass through
     */
    boolean wrap() default true;
}
