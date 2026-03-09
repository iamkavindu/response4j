package io.github.response4j.core.annotation;

import io.github.response4j.core.model.ProblemDetail;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares a problem-specific extension member to be included in the RFC 9457
 * {@link ProblemDetail} response.
 * <p>
 * Per RFC 9457, producers may add additional members (extensions) to convey problem-specific
 * information beyond the standard fields. This annotation allows statically declaring these
 * extensions on exception classes, which are then automatically included in the problem detail
 * response as top-level JSON properties.
 * <p>
 * This annotation is {@link Repeatable}, so multiple extensions can be declared on a single
 * exception class by applying the annotation multiple times or using the {@link ProblemExtensions}
 * container annotation.
 * <p>
 * Example usage:
 * <pre>{@code
 * @ProblemResponse(status = 400, title = "Validation Error",
 *     type = "https://api.example.com/errors/validation")
 * @ProblemExtension(key = "docs", value = "https://api.example.com/docs/errors/validation")
 * @ProblemExtension(key = "supportEmail", value = "support@example.com")
 * public class ValidationException extends RuntimeException {
 *     // ...
 * }
 * }</pre>
 *
 * @see ProblemExtensions
 * @see ProblemResponse
 * @see ProblemDetail
 * @see <a href="https://www.rfc-editor.org/rfc/rfc9457#section-3">RFC 9457 Section 3</a>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(ProblemExtensions.class)
public @interface ProblemExtension {
    /**
     * The extension field name (key) to be included in the problem detail response.
     * <p>
     * This will appear as a top-level JSON property alongside the standard RFC 9457 fields.
     *
     * @return the extension field name
     */
    String key();

    /**
     * The extension field value.
     * <p>
     * This will be serialized as a string in the problem detail response.
     *
     * @return the extension field value
     */
    String value();
}
