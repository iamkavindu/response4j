package io.github.iamkavindu.response4j.quarkus.producer;

import io.github.iamkavindu.response4j.mapper.ApiResponseMapper;
import io.github.iamkavindu.response4j.mapper.ProblemDetailMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class Response4jProducer {

    @Produces
    @ApplicationScoped
    public ProblemDetailMapper problemDetailMapper() {
        return new ProblemDetailMapper();
    }

    @Produces
    @ApplicationScoped
    public ApiResponseMapper apiResponseMapper() {
        return new ApiResponseMapper();
    }
}
