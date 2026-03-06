package io.github.response4j.spring.autoconfigure;

import io.github.response4j.core.mapper.ApiResponseMapper;
import io.github.response4j.core.mapper.ProblemDetailMapper;
import io.github.response4j.spring.advice.Response4jResponseBodyAdvice;
import io.github.response4j.spring.handler.Response4jExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

/**
 * Spring Boot auto-configuration for response4j.
 * <p>
 * Registers mappers, exception handler, and response body advice when running in a web application.
 * All beans are conditional on {@code @ConditionalOnMissingBean}, allowing user overrides.
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
     * Registers {@link ApiResponseMapper} unless a bean is already present.
     *
     * @return the API response mapper
     */
    @Bean
    @ConditionalOnMissingBean
    public ApiResponseMapper apiResponseMapper() {
        return new ApiResponseMapper();
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

    /**
     * Registers {@link Response4jResponseBodyAdvice} unless a bean is already present.
     *
     * @param apiResponseMapper the API response mapper (injected)
     * @return the response body advice
     */
    @Bean
    @ConditionalOnMissingBean
    public Response4jResponseBodyAdvice response4jResponseBodyAdvice(ApiResponseMapper apiResponseMapper) {
        return new Response4jResponseBodyAdvice(apiResponseMapper);
    }
}
