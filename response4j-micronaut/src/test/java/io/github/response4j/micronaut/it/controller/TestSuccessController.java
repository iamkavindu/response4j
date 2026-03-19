package io.github.response4j.micronaut.it.controller;

import java.util.Map;

import io.github.response4j.core.annotation.SuccessResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

@Controller("/test/success")
@Produces(MediaType.APPLICATION_JSON)
@SuccessResponse(status = 201, message = "Class level created")
public class TestSuccessController {

    @Get("/method")
    @SuccessResponse(status = 200, message = "Method level OK")
    public Map<String, Object> methodLevel() {
        return Map.of("value", "method");
    }

    @Get("/class")
    public Map<String, Object> classLevel() {
        return Map.of("value", "class");
    }

    @Get("/wrap-false")
    @SuccessResponse(status = 200, message = "Ignored", wrap = false)
    public Map<String, Object> wrapFalse() {
        return Map.of("wrapped", false);
    }
}
