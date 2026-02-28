package io.github.iamkavindu.response4j.quarkus.mapper;

import io.github.iamkavindu.response4j.mapper.ProblemDetailMapper;
import io.github.iamkavindu.response4j.model.ProblemDetail;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * JAX-RS {@link ExceptionMapper} that produces RFC 7807 {@link ProblemDetail} responses
 * for all exceptions.
 * <p>
 * Returns responses with {@code Content-Type: application/problem+json} and the appropriate
 * HTTP status from the mapped problem detail (or 500 for unmapped exceptions).
 */
@Provider
@ApplicationScoped
public class Response4jExceptionMapper implements ExceptionMapper<Exception> {

    private final ProblemDetailMapper problemDetailMapper;

    /**
     * Creates the mapper with the given {@link ProblemDetailMapper}.
     *
     * @param problemDetailMapper the mapper for converting exceptions to ProblemDetail
     */
    @Inject
    public Response4jExceptionMapper(ProblemDetailMapper problemDetailMapper) {
        this.problemDetailMapper = problemDetailMapper;
    }

    /**
     * Maps the exception to a JAX-RS response containing the RFC 7807 problem detail.
     *
     * @param exception the thrown exception
     * @return a response with {@code Content-Type: application/problem+json} and the mapped status
     */
    @Override
    public Response toResponse(Exception exception) {
        ProblemDetail problemDetail = problemDetailMapper.map(exception);
        return Response
                .status(problemDetail.status())
                .type("application/problem+json")
                .entity(problemDetail)
                .build();
    }
}
