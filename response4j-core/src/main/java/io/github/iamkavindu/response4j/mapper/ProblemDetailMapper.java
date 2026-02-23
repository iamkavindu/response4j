package io.github.iamkavindu.response4j.mapper;

import io.github.iamkavindu.response4j.annotation.ProblemResponse;
import io.github.iamkavindu.response4j.model.ProblemDetail;

import java.net.URI;
import java.util.Objects;


public class ProblemDetailMapper {

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

        return ProblemDetail.builder()
                .type(URI.create(annotation.type()))
                .status(annotation.status())
                .title(title)
                .detail(detail)
                .build();
    }

    private ProblemDetail mapWithDefaults(Throwable exception) {
        return ProblemDetail.builder()
                .type(URI.create("about:blank"))
                .status(500)
                .title("Internal Server Error")
                .detail(Objects.requireNonNullElse(exception.getMessage(), ""))
                .build();
    }
}
