package io.github.iamkavindu.response4j.spring.handler;

import io.github.iamkavindu.response4j.mapper.ProblemDetailMapper;
import io.github.iamkavindu.response4j.model.ProblemDetail;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler that produces RFC 7807 {@link ProblemDetail} responses for all exceptions.
 * <p>
 * Returns responses with {@code Content-Type: application/problem+json} and the appropriate
 * HTTP status from the mapped problem detail (or 500 for unmapped exceptions).
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
     *
     * @param exception the thrown exception
     * @return a response entity with the mapped ProblemDetail and appropriate status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleException(Exception exception) {
        ProblemDetail problemDetail = problemDetailMapper.map(exception);
        return ResponseEntity
                .status(problemDetail.status())
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problemDetail);
    }
}
