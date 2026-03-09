package io.github.response4j.core.mapper;

import io.github.response4j.core.annotation.ProblemExtension;
import io.github.response4j.core.annotation.ProblemResponse;
import io.github.response4j.core.model.ProblemDetail;
import io.github.response4j.core.model.ProblemTypes;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Maps {@link Throwable} instances to {@link ProblemDetail} RFC 9457 representations.
 * <p>
 * When the exception class is annotated with {@link ProblemResponse}, uses the annotation values
 * (with fallbacks for blank title/detail). Otherwise, returns a generic 500 "Internal Server Error"
 * problem detail.
 * <p>
 * Per RFC 9457 Section 4.2.1, when the type is {@code about:blank}, the title MUST be the HTTP
 * reason phrase for the status code (e.g., "Not Found" for 404).
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
        return map(exception, null);
    }

    /**
     * Maps the given exception to a {@code ProblemDetail} with a specific instance URI.
     * <p>
     * If the exception class has {@link ProblemResponse}, uses its status, title, type, and detail.
     * Blank title defaults to the exception class simple name. Blank detail defaults to the
     * exception message when {@code includeExceptionMessage} is true.
     * <p>
     * The {@code instance} parameter is used to populate the RFC 9457 "instance" field, which
     * identifies the specific occurrence of the problem. Typically, this is set to the request URI
     * that triggered the error.
     *
     * @param exception the exception to map
     * @param instance  the URI reference identifying the specific occurrence of the problem, or {@code null}
     * @return a {@code ProblemDetail} representing the error with the instance field set
     */
    public ProblemDetail map(Throwable exception, String instance) {
        ProblemResponse annotation = exception.getClass().getAnnotation(ProblemResponse.class);

        if (annotation != null) {
            return mapFromAnnotation(exception, annotation, instance);
        }

        return mapWithDefaults(exception, instance);
    }

    /**
     * Resolves the HTTP reason phrase for the given status code.
     * <p>
     * This method returns the canonical HTTP reason phrase as defined in RFC 9110.
     * For unknown status codes, it returns "HTTP Error {status}".
     *
     * @param status the HTTP status code
     * @return the HTTP reason phrase for the status code
     */
    private static String httpReasonPhrase(int status) {
        return switch (status) {
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 402 -> "Payment Required";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 405 -> "Method Not Allowed";
            case 406 -> "Not Acceptable";
            case 407 -> "Proxy Authentication Required";
            case 408 -> "Request Timeout";
            case 409 -> "Conflict";
            case 410 -> "Gone";
            case 411 -> "Length Required";
            case 412 -> "Precondition Failed";
            case 413 -> "Content Too Large";
            case 414 -> "URI Too Long";
            case 415 -> "Unsupported Media Type";
            case 416 -> "Range Not Satisfiable";
            case 417 -> "Expectation Failed";
            case 421 -> "Misdirected Request";
            case 422 -> "Unprocessable Content";
            case 423 -> "Locked";
            case 424 -> "Failed Dependency";
            case 425 -> "Too Early";
            case 426 -> "Upgrade Required";
            case 428 -> "Precondition Required";
            case 429 -> "Too Many Requests";
            case 431 -> "Request Header Fields Too Large";
            case 451 -> "Unavailable For Legal Reasons";
            case 500 -> "Internal Server Error";
            case 501 -> "Not Implemented";
            case 502 -> "Bad Gateway";
            case 503 -> "Service Unavailable";
            case 504 -> "Gateway Timeout";
            case 505 -> "HTTP Version Not Supported";
            case 506 -> "Variant Also Negotiates";
            case 507 -> "Insufficient Storage";
            case 508 -> "Loop Detected";
            case 510 -> "Not Extended";
            case 511 -> "Network Authentication Required";
            default -> "HTTP Error " + status;
        };
    }

    private ProblemDetail mapFromAnnotation(Throwable exception, ProblemResponse annotation, String instance) {
        URI type = URI.create(annotation.type());
        int status = annotation.status();

        // Per RFC 9457 Section 4.2.1: when type is about:blank, title MUST be HTTP reason phrase
        String title;
        if (ProblemTypes.ABOUT_BLANK.equals(type) && annotation.title().isBlank()) {
            title = httpReasonPhrase(status);
        } else {
            title = annotation.title().isBlank() ? exception.getClass().getSimpleName() : annotation.title();
        }

        String detail = annotation.detail().isBlank() && annotation.includeExceptionMessage()
                ? Objects.requireNonNullElse(exception.getMessage(), "")
                : annotation.detail();

        // Extract extension members from @ProblemExtension annotations
        Map<String, Object> extensions = extractExtensions(exception);

        return new ProblemDetail.Builder()
                .type(type)
                .status(status)
                .title(title)
                .detail(detail)
                .instance(instance)
                .extensions(extensions.isEmpty() ? null : extensions)
                .build();
    }

    /**
     * Extracts problem-specific extension members from {@link ProblemExtension} annotations
     * on the exception class.
     *
     * @param exception the exception to extract extensions from
     * @return a map of extension field names to values; empty if no extensions are present
     */
    private Map<String, Object> extractExtensions(Throwable exception) {
        Map<String, Object> extensions = new HashMap<>();

        ProblemExtension[] extensionAnnotations = exception.getClass().getAnnotationsByType(ProblemExtension.class);

        for (ProblemExtension ext : extensionAnnotations) {
            extensions.put(ext.key(), ext.value());
        }

        return extensions;
    }

    private ProblemDetail mapWithDefaults(Throwable exception, String instance) {
        int status = 500;
        return new ProblemDetail.Builder()
                .type(ProblemTypes.ABOUT_BLANK)
                .status(status)
                .title(httpReasonPhrase(status))
                .detail(Objects.requireNonNullElse(exception.getMessage(), ""))
                .instance(instance)
                .build();
    }
}
