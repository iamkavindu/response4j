package io.github.iamkavindu.response4j.spring.autoconfigure;

import io.github.iamkavindu.response4j.mapper.ApiResponseMapper;
import io.github.iamkavindu.response4j.mapper.ProblemDetailMapper;
import io.github.iamkavindu.response4j.spring.advice.Response4jResponseBodyAdvice;
import io.github.iamkavindu.response4j.spring.handler.Response4jExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnWebApplication
public class Response4jAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ProblemDetailMapper problemDetailMapper() {
        return new ProblemDetailMapper();
    }

    @Bean
    @ConditionalOnMissingBean
    public ApiResponseMapper apiResponseMapper() {
        return new ApiResponseMapper();
    }

    @Bean
    @ConditionalOnMissingBean
    public Response4jExceptionHandler response4jExceptionHandler(ProblemDetailMapper problemDetailMapper) {
        return new Response4jExceptionHandler(problemDetailMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public Response4jResponseBodyAdvice response4jResponseBodyAdvice(ApiResponseMapper apiResponseMapper) {
        return new Response4jResponseBodyAdvice(apiResponseMapper);
    }
}
