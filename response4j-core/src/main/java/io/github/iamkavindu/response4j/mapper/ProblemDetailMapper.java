package io.github.iamkavindu.response4j.mapper;

import io.github.iamkavindu.response4j.annotation.ProblemResponse;
import io.github.iamkavindu.response4j.model.ProblemDetail;

import java.net.URI;
import java.util.Objects;

/**
 * Maps {@link Throwable} instances to {@link ProblemDetail} RFC 7807 representations.
 * <p>
 * When the exception class is annotated with {@link ProblemResponse}, uses the annotation values
 * (with fallbacks for blank title/detail). Otherwise, returns a generic 500 "Internal Server Error"
 * problem detail.
 */
public class ProblemDetailMapper {

    /**
     * Maps the given exception to a {@code ProblemDetail}.
     * <p>
     * If the exception class has {@link ProblemResponse}, uses its status, title, type, and detail.
     * Blank title defaults to the exception class simple name. Blank detail defaults to the
     * exception message when {@code includeExceptionMessage} is true.
     *
     * @param exception the exception to map
     * @return a {@code ProblemDetail} representing the error
     */
    public ProblemDetail map(Throwable exception) {
        ProblemResponse annotation = exception.getClass()
                .getAnnotation(ProblemResponse.class);

        if (annotation != null) {
            return mapFromAnnotation(exception, annotation);
        }

        return mapWithDefaults(exception);
    }

    private ProblemDetail mapFromAnnotation(Throwable exception, ProblemResponse annotation) {
        String title = annotation.title().isBlank()
                ? exception.getClass().getSimpleName()
                : annotation.title();

        String detail = annotation.detail().isBlank() && annotation.includeExceptionMessage()
                ? Objects.requireNonNullElse(exception.getMessage(), "")
                : annotation.detail();

        return new ProblemDetail.Builder()
                .type(URI.create(annotation.type()))
                .status(annotation.status())
                .title(title)
                .detail(detail)
                .build();
    }

    private ProblemDetail mapWithDefaults(Throwable exception) {
        return new ProblemDetail.Builder()
                .type(URI.create("about:blank"))
                .status(500)
                .title("Internal Server Error")
                .detail(Objects.requireNonNullElse(exception.getMessage(), ""))
                .build();
    }
}
