package io.github.iamkavindu.response4j.micronaut.it.controller;

import io.github.iamkavindu.response4j.annotation.ProblemResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/test/error")
public class TestErrorController {

    @ProblemResponse(
            status = 400,
            title = "Bad Request",
            type = "https://api.example.com/problems/bad-request",
            detail = "Request is invalid",
            includeExceptionMessage = false
    )
    public static class AnnotatedBadRequestException extends RuntimeException {
    }

    @ProblemResponse(
            status = 404,
            title = "",
            type = "about:blank",
            detail = "",
            includeExceptionMessage = false
    )
    public static class AboutBlankNotFoundException extends RuntimeException {
    }

    public static class UnannotatedException extends RuntimeException {
        public UnannotatedException(String message) {
            super(message);
        }
    }

    @Get("/annotated")
    public void annotated() {
        throw new AnnotatedBadRequestException();
    }

    @Get("/about-blank")
    public void aboutBlank() {
        throw new AboutBlankNotFoundException();
    }

    @Get("/unannotated")
    public void unannotated() {
        throw new UnannotatedException("failure");
    }
}

