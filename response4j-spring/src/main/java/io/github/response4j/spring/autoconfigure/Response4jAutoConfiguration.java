package io.github.response4j.spring.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

import io.github.response4j.core.mapper.ProblemDetailMapper;
import io.github.response4j.spring.handler.Response4jExceptionHandler;

/**
 * Spring Boot auto-configuration for response4j.
 * <p>
 * Registers the {@link ProblemDetailMapper} and {@link Response4jExceptionHandler} when running
 * in a web application. All beans are conditional on {@code @ConditionalOnMissingBean}, allowing
 * user overrides.
 */
@AutoConfiguration
@ConditionalOnWebApplication
public class Response4jAutoConfiguration {

    /**
     * Registers {@link ProblemDetailMapper} unless a bean is already present.
     *
     * @return the problem detail mapper
     */
    @Bean
    @ConditionalOnMissingBean
    public ProblemDetailMapper problemDetailMapper() {
        return new ProblemDetailMapper();
    }

    /**
     * Registers {@link Response4jExceptionHandler} unless a bean is already present.
     *
     * @param problemDetailMapper the problem detail mapper (injected)
     * @return the exception handler
     */
    @Bean
    @ConditionalOnMissingBean
    public Response4jExceptionHandler response4jExceptionHandler(ProblemDetailMapper problemDetailMapper) {
        return new Response4jExceptionHandler(problemDetailMapper);
    }
}
