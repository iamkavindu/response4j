package io.github.response4j.micronaut.it.controller;

import io.github.response4j.core.annotation.SuccessResponse;
import io.github.response4j.core.model.ApiResponse;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import java.util.Map;

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

    @Get("/already-wrapped")
    @SuccessResponse(status = 200, message = "Ignored")
    public HttpResponse<ApiResponse<String>> alreadyWrapped() {
        ApiResponse<String> body = ApiResponse.ok("wrapped");
        return HttpResponse.ok(body);
    }
}
