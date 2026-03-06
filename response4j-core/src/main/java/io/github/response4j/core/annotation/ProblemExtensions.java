package io.github.response4j.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Container annotation for multiple {@link ProblemExtension} annotations.
 * <p>
 * This annotation is automatically applied by the Java compiler when multiple
 * {@link ProblemExtension} annotations are used on the same element, due to the
 * {@link java.lang.annotation.Repeatable} meta-annotation on {@link ProblemExtension}.
 * <p>
 * You typically don't need to use this annotation directly; instead, apply multiple
 * {@link ProblemExtension} annotations, and the compiler will generate this container
 * annotation automatically.
 * <p>
 * Example usage (the compiler handles this automatically):
 * <pre>{@code
 * @ProblemResponse(status = 400, title = "Validation Error")
 * @ProblemExtension(key = "docs", value = "https://api.example.com/docs")
 * @ProblemExtension(key = "supportEmail", value = "support@example.com")
 * public class ValidationException extends RuntimeException {
 *     // ...
 * }
 * }</pre>
 *
 * @see ProblemExtension
 * @see ProblemResponse
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProblemExtensions {
    /**
     * The array of {@link ProblemExtension} annotations.
     *
     * @return the contained extension annotations
     */
    ProblemExtension[] value();
}
