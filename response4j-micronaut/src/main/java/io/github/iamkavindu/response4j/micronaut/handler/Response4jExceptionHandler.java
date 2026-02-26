package io.github.iamkavindu.response4j.micronaut.handler;

import io.github.iamkavindu.response4j.mapper.ProblemDetailMapper;
import io.github.iamkavindu.response4j.model.ProblemDetail;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

/**
 * Micronaut exception handler that produces RFC 7807 {@link ProblemDetail} responses for all exceptions.
 * <p>
 * Returns responses with {@code Content-Type: application/problem+json} and the appropriate
 * HTTP status from the mapped problem detail (or 500 for unmapped exceptions).
 */
@Singleton
@Produces(MediaType.APPLICATION_JSON)
@Requires(classes = {ExceptionHandler.class})
public class Response4jExceptionHandler implements ExceptionHandler<Exception, HttpResponse<ProblemDetail>> {

    private final ProblemDetailMapper problemDetailMapper;

    /**
     * Creates the handler with the given mapper.
     *
     * @param problemDetailMapper the mapper for converting exceptions to ProblemDetail
     */
    @Inject
    public Response4jExceptionHandler(ProblemDetailMapper problemDetailMapper) {
        this.problemDetailMapper = problemDetailMapper;
    }

    /**
     * Handles the exception, mapping it to ProblemDetail and returning as
     * {@code application/problem+json}.
     *
     * @param request   the HTTP request
     * @param exception the thrown exception
     * @return an HTTP response with the mapped ProblemDetail and appropriate status
     */
    @Override
    public HttpResponse<ProblemDetail> handle(HttpRequest request, Exception exception) {
        ProblemDetail problemDetail = problemDetailMapper.map(exception);

        return HttpResponse
                .<ProblemDetail>status(
                        HttpStatus.valueOf(problemDetail.getStatus())
                )
                .contentType("application/problem+json")
                .body(problemDetail);
    }
}
