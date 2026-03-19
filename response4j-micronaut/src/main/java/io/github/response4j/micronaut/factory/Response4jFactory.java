package io.github.response4j.micronaut.factory;

import io.github.response4j.core.mapper.ProblemDetailMapper;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;

/**
 * Micronaut {@link io.micronaut.context.annotation.Factory} that provides
 * {@link ProblemDetailMapper} as a bean for Micronaut HTTP applications.
 */
@Factory
@Requires(classes = {HttpRequest.class})
public class Response4jFactory {

    /**
     * Produces a {@link ProblemDetailMapper} for exception-to-RFC-9457 mapping.
     *
     * @return the problem detail mapper instance
     */
    @Bean
    @Primary
    public ProblemDetailMapper problemDetailMapper() {
        return new ProblemDetailMapper();
    }
}
