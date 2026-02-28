package io.github.iamkavindu.response4j.model;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ProblemDetail} record.
 */
class ProblemDetailTest {

    @Test
    void ofShouldCreateProblemDetailWithAboutBlank() {
        ProblemDetail problem = ProblemDetail.of(
            "Not Found",
            404,
            "User with id 42 not found",
            "/users/42",
            null
        );

        assertEquals(ProblemTypes.ABOUT_BLANK, problem.type());
        assertEquals("Not Found", problem.title());
        assertEquals(404, problem.status());
        assertEquals("User with id 42 not found", problem.detail());
        assertEquals("/users/42", problem.instance());
        assertNull(problem.extensions());
    }

    @Test
    void ofShouldSupportExtensions() {
        Map<String, Object> extensions = Map.of(
            "traceId", "abc-123",
            "timestamp", "2024-01-15T10:30:00Z"
        );

        ProblemDetail problem = ProblemDetail.of(
            "Internal Server Error",
            500,
            "Database connection failed",
            "/api/users",
            extensions
        );

        assertNotNull(problem.extensions());
        assertEquals("abc-123", problem.extensions().get("traceId"));
        assertEquals("2024-01-15T10:30:00Z", problem.extensions().get("timestamp"));
    }

    @Test
    void ofErrorsShouldCreateProblemDetailWithErrorsList() {
        List<ProblemDetailError> errors = List.of(
            new ProblemDetailError("/email", "must be a valid email address"),
            new ProblemDetailError("/age", "must be at least 18")
        );

        ProblemDetail problem = ProblemDetail.ofErrors(
            "Validation Failed",
            400,
            "The request contains invalid fields",
            errors
        );

        assertEquals(ProblemTypes.ABOUT_BLANK, problem.type());
        assertEquals("Validation Failed", problem.title());
        assertEquals(400, problem.status());
        assertEquals("The request contains invalid fields", problem.detail());
        assertNotNull(problem.extensions());
        assertTrue(problem.extensions().containsKey("errors"));
        
        @SuppressWarnings("unchecked")
        List<ProblemDetailError> extractedErrors = (List<ProblemDetailError>) problem.extensions().get("errors");
        assertEquals(2, extractedErrors.size());
        assertEquals("/email", extractedErrors.getFirst().pointer());
        assertEquals("must be a valid email address", extractedErrors.getFirst().detail());
    }

    @Test
    void ofErrorsShouldHandleEmptyErrorsList() {
        List<ProblemDetailError> errors = List.of();

        ProblemDetail problem = ProblemDetail.ofErrors(
            "Validation Failed",
            400,
            "The request contains invalid fields",
            errors
        );

        assertNotNull(problem.extensions());
        assertTrue(problem.extensions().containsKey("errors"));
        
        @SuppressWarnings("unchecked")
        List<ProblemDetailError> extractedErrors = (List<ProblemDetailError>) problem.extensions().get("errors");
        assertTrue(extractedErrors.isEmpty());
    }

    @Test
    void builderShouldCreateProblemDetail() {
        ProblemDetail problem = new ProblemDetail.Builder()
            .type(ProblemTypes.ABOUT_BLANK)
            .title("Bad Request")
            .status(400)
            .detail("Invalid input")
            .instance("/api/test")
            .extensions(Map.of("custom", "value"))
            .build();

        assertEquals(ProblemTypes.ABOUT_BLANK, problem.type());
        assertEquals("Bad Request", problem.title());
        assertEquals(400, problem.status());
        assertEquals("Invalid input", problem.detail());
        assertEquals("/api/test", problem.instance());
        assertNotNull(problem.extensions());
        assertEquals("value", problem.extensions().get("custom"));
    }
}
