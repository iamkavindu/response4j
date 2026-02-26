package io.github.iamkavindu.response4j.micronaut.factory;

import io.github.iamkavindu.response4j.mapper.ApiResponseMapper;
import io.github.iamkavindu.response4j.mapper.ProblemDetailMapper;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;

/**
 * Micronaut {@link io.micronaut.context.annotation.Factory} that provides
 * {@link ProblemDetailMapper} and {@link ApiResponseMapper} as beans for Micronaut HTTP applications.
 */
@Factory
@Requires(classes = {HttpRequest.class})
public class Response4jFactory {

    /**
     * Produces a {@link ProblemDetailMapper} for exception-to-RFC-7807 mapping.
     *
     * @return the problem detail mapper instance
     */
    @Bean
    @Primary
    public ProblemDetailMapper problemDetailMapper() {
        return new ProblemDetailMapper();
    }

    /**
     * Produces an {@link ApiResponseMapper} for wrapping success responses.
     *
     * @return the API response mapper instance
     */
    @Bean
    @Primary
    public ApiResponseMapper apiResponseMapper() {
        return new ApiResponseMapper();
    }
}
