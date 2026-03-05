package io.github.iamkavindu.response4j.spring.it.controller;

import io.github.iamkavindu.response4j.annotation.ProblemResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/error")
public class TestErrorController {

    @ProblemResponse(
            status = 400,
            title = "Bad Request",
            type = "https://api.example.com/problems/bad-request",
            detail = "Request is invalid",
            includeExceptionMessage = false
    )
    static class AnnotatedBadRequestException extends RuntimeException {
        AnnotatedBadRequestException() {
            super("ignored");
        }
    }

    @ProblemResponse(
            status = 404,
            title = "",
            type = "about:blank",
            detail = "",
            includeExceptionMessage = false
    )
    static class AboutBlankNotFoundException extends RuntimeException {
        AboutBlankNotFoundException() {
            super("ignored");
        }
    }

    static class UnannotatedException extends RuntimeException {
        UnannotatedException(String message) {
            super(message);
        }
    }

    @GetMapping("/annotated")
    public void annotated() {
        throw new AnnotatedBadRequestException();
    }

    @GetMapping("/about-blank")
    public void aboutBlank() {
        throw new AboutBlankNotFoundException();
    }

    @GetMapping("/unannotated")
    public void unannotated() {
        throw new UnannotatedException("failure");
    }
}

