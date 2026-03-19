package io.github.response4j.quarkus.producer;

import io.github.response4j.core.mapper.ProblemDetailMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

/**
 * CDI producer that provides {@link ProblemDetailMapper} as an application-scoped bean
 * for Quarkus/JAX-RS applications.
 */
@ApplicationScoped
public class Response4jProducer {

    /**
     * Produces a {@link ProblemDetailMapper} for exception-to-RFC-9457 mapping.
     *
     * @return the problem detail mapper instance
     */
    @Produces
    @ApplicationScoped
    public ProblemDetailMapper problemDetailMapper() {
        return new ProblemDetailMapper();
    }
}
