package io.github.iamkavindu.response4j.quarkus.producer;

import io.github.iamkavindu.response4j.mapper.ApiResponseMapper;
import io.github.iamkavindu.response4j.mapper.ProblemDetailMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

/**
 * CDI producer that provides {@link ProblemDetailMapper} and {@link ApiResponseMapper}
 * as application-scoped beans for Quarkus/JAX-RS applications.
 */
@ApplicationScoped
public class Response4jProducer {

    /**
     * Produces a {@link ProblemDetailMapper} for exception-to-RFC-7807 mapping.
     *
     * @return the problem detail mapper instance
     */
    @Produces
    @ApplicationScoped
    public ProblemDetailMapper problemDetailMapper() {
        return new ProblemDetailMapper();
    }

    /**
     * Produces an {@link ApiResponseMapper} for wrapping success responses.
     *
     * @return the API response mapper instance
     */
    @Produces
    @ApplicationScoped
    public ApiResponseMapper apiResponseMapper() {
        return new ApiResponseMapper();
    }
}
