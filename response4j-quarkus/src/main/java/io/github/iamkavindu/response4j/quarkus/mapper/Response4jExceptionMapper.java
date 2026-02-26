package io.github.iamkavindu.response4j.quarkus.mapper;

import io.github.iamkavindu.response4j.mapper.ProblemDetailMapper;
import io.github.iamkavindu.response4j.model.ProblemDetail;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class Response4jExceptionMapper implements ExceptionMapper<Exception> {

    private final ProblemDetailMapper problemDetailMapper;

    @Inject
    public Response4jExceptionMapper(ProblemDetailMapper problemDetailMapper) {
        this.problemDetailMapper = problemDetailMapper;
    }

    @Override
    public Response toResponse(Exception exception) {
        ProblemDetail problemDetail = problemDetailMapper.map(exception);
        return Response
                .status(problemDetail.getStatus())
                .type("application/problem+json")
                .entity(problemDetail)
                .build();
    }
}
