package io.github.iamkavindu.response4j.spring.handler;

import io.github.iamkavindu.response4j.mapper.ProblemDetailMapper;
import io.github.iamkavindu.response4j.model.ProblemDetail;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler that produces RFC 9457 {@link ProblemDetail} responses for all exceptions.
 * <p>
 * Returns responses with {@code Content-Type: application/problem+json} and the appropriate
 * HTTP status from the mapped problem detail (or 500 for unmapped exceptions).
 * <p>
 * Per RFC 9457 Section 3.1, the {@code instance} field is populated with the request URI to help
 * identify the specific occurrence of the problem.
 */
@RestControllerAdvice
public class Response4jExceptionHandler {
    private final ProblemDetailMapper problemDetailMapper;

    /**
     * Creates the handler with the given mapper.
     *
     * @param problemDetailMapper the mapper for converting exceptions to ProblemDetail
     */
    public Response4jExceptionHandler(ProblemDetailMapper problemDetailMapper) {
        this.problemDetailMapper = problemDetailMapper;
    }

    /**
     * Handles all exceptions, mapping them to ProblemDetail and returning as
     * {@code application/problem+json}.
     * <p>
     * The request URI is passed to the mapper to populate the {@code instance} field,
     * which identifies the specific occurrence of the problem per RFC 9457.
     *
     * @param request   the HTTP request that triggered the exception
     * @param exception the thrown exception
     * @return a response entity with the mapped ProblemDetail and appropriate status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleException(HttpRequest request, Exception exception) {
        String instance = request.getURI().toString();
        ProblemDetail problemDetail = problemDetailMapper.map(exception, instance);
        return ResponseEntity
                .status(problemDetail.status())
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problemDetail);
    }
}
