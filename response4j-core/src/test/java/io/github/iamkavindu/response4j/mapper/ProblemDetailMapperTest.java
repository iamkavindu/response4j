package io.github.iamkavindu.response4j.mapper;

import io.github.iamkavindu.response4j.annotation.ProblemExtension;
import io.github.iamkavindu.response4j.annotation.ProblemResponse;
import io.github.iamkavindu.response4j.model.ProblemDetail;
import io.github.iamkavindu.response4j.model.ProblemTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ProblemDetailMapper}.
 */
class ProblemDetailMapperTest {

    private ProblemDetailMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProblemDetailMapper();
    }

    @Test
    void mapShouldReturnInternalServerErrorForUnAnnotatedException() {
        Exception exception = new RuntimeException("Something went wrong");

        ProblemDetail problem = mapper.map(exception);

        assertEquals(ProblemTypes.ABOUT_BLANK, problem.type());
        assertEquals("Internal Server Error", problem.title()); // RFC 9457: about:blank requires HTTP reason phrase
        assertEquals(500, problem.status());
        assertEquals("Something went wrong", problem.detail());
        assertNull(problem.instance());
    }

    @Test
    void mapShouldUseAnnotationValues() {
        Exception exception = new TestNotFoundException(42L);

        ProblemDetail problem = mapper.map(exception);

        assertEquals(URI.create("https://api.example.com/errors/not-found"), problem.type());
        assertEquals("Resource Not Found", problem.title());
        assertEquals(404, problem.status());
        assertEquals("User with id 42 not found", problem.detail());
    }

    @Test
    void mapShouldUseHttpReasonPhraseWhenTypeIsAboutBlankAndTitleIsBlank() {
        Exception exception = new TestBadRequestException();

        ProblemDetail problem = mapper.map(exception);

        assertEquals(ProblemTypes.ABOUT_BLANK, problem.type());
        assertEquals("Bad Request", problem.title()); // RFC 9457 Section 4.2.1
        assertEquals(400, problem.status());
    }

    @Test
    void mapShouldUseExceptionClassNameWhenTitleIsBlankAndTypeIsNotAboutBlank() {
        Exception exception = new TestValidationException();

        ProblemDetail problem = mapper.map(exception);

        assertEquals(URI.create("https://api.example.com/errors/validation"), problem.type());
        assertEquals("TestValidationException", problem.title()); // Falls back to class name
        assertEquals(400, problem.status());
    }

    @Test
    void mapWithInstanceShouldPopulateInstanceField() {
        Exception exception = new TestNotFoundException(42L);

        ProblemDetail problem = mapper.map(exception, "/api/users/42");

        assertEquals("/api/users/42", problem.instance());
    }

    @Test
    void mapShouldIncludeExtensionsFromAnnotation() {
        Exception exception = new TestExceptionWithExtensions();

        ProblemDetail problem = mapper.map(exception);

        assertNotNull(problem.extensions());
        assertEquals("https://api.example.com/docs", problem.extensions().get("docs"));
        assertEquals("support@example.com", problem.extensions().get("supportEmail"));
    }

    @Test
    void mapShouldHandleMultipleExtensions() {
        Exception exception = new TestExceptionWithMultipleExtensions();

        ProblemDetail problem = mapper.map(exception);

        assertNotNull(problem.extensions());
        assertEquals(3, problem.extensions().size());
        assertEquals("value1", problem.extensions().get("key1"));
        assertEquals("value2", problem.extensions().get("key2"));
        assertEquals("value3", problem.extensions().get("key3"));
    }

    @Test
    void mapShouldNotIncludeExtensionsWhenNonePresent() {
        Exception exception = new TestNotFoundException(42L);

        ProblemDetail problem = mapper.map(exception);

        assertNull(problem.extensions());
    }

    @Test
    void mapShouldUseHttpReasonPhraseFor404() {
        Exception exception = new TestNotFoundException(42L);

        ProblemDetail problem = mapper.map(exception);

        // Even though annotation has a custom type, title is set
        assertEquals("Resource Not Found", problem.title());
    }

    @Test
    void mapShouldHandleNullExceptionMessage() {
        Exception exception = new TestExceptionWithNullMessage();

        ProblemDetail problem = mapper.map(exception);

        assertEquals("", problem.detail());
    }

    // Test exception classes

    @ProblemResponse(
            status = 404,
            title = "Resource Not Found",
            type = "https://api.example.com/errors/not-found"
    )
    static class TestNotFoundException extends RuntimeException {
        public TestNotFoundException(Long id) {
            super("User with id " + id + " not found");
        }
    }

    @ProblemResponse(
            status = 400
    )
    static class TestBadRequestException extends RuntimeException {
        public TestBadRequestException() {
            super("Invalid request");
        }
    }

    @ProblemResponse(
            status = 400,
            type = "https://api.example.com/errors/validation"
            // title is blank but type is not about:blank, should use class name
    )
    static class TestValidationException extends RuntimeException {
        public TestValidationException() {
            super("Validation failed");
        }
    }

    @ProblemResponse(
            status = 400,
            title = "Test Error"
    )
    @ProblemExtension(key = "docs", value = "https://api.example.com/docs")
    @ProblemExtension(key = "supportEmail", value = "support@example.com")
    static class TestExceptionWithExtensions extends RuntimeException {
        public TestExceptionWithExtensions() {
            super("Test error with extensions");
        }
    }

    @ProblemResponse(
            title = "Internal Error"
    )
    @ProblemExtension(key = "key1", value = "value1")
    @ProblemExtension(key = "key2", value = "value2")
    @ProblemExtension(key = "key3", value = "value3")
    static class TestExceptionWithMultipleExtensions extends RuntimeException {
        public TestExceptionWithMultipleExtensions() {
            super("Multiple extensions");
        }
    }

    @ProblemResponse(
            title = "Null Message Error"
    )
    static class TestExceptionWithNullMessage extends RuntimeException {
        public TestExceptionWithNullMessage() {
            super((String) null);
        }
    }
}
