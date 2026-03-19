package io.github.response4j.spring.it.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.response4j.core.annotation.SuccessResponse;

/**
 * Test-only controller that exercises response4j success wrapping
 * via {@link SuccessResponse} on methods and classes.
 */
@RestController
@RequestMapping("/test/success")
@SuccessResponse(status = 201, message = "Class level created")
public class TestSuccessController {

    @GetMapping("/method")
    @SuccessResponse(status = 200, message = "Method level OK")
    public Map<String, Object> methodLevel() {
        return Map.of("value", "method");
    }

    @GetMapping("/class")
    public Map<String, Object> classLevel() {
        return Map.of("value", "class");
    }

    @GetMapping("/wrap-false")
    @SuccessResponse(status = 200, message = "Ignored", wrap = false)
    public Map<String, Object> wrapFalse() {
        return Map.of("wrapped", false);
    }

    @GetMapping("/no-content")
    @SuccessResponse(status = 204, message = "No Content")
    public void noContent() {
        // Intentionally empty; ResponseBodyAdvice should set 204 and omit body.
    }
}
